package com.boersenparty.v_1_1.service;
import com.boersenparty.v_1_1.events.PriceUpdateEvent;
import com.boersenparty.v_1_1.models.CalculatedPrice;
import com.boersenparty.v_1_1.models.Product;
import com.boersenparty.v_1_1.repository.CalculatedPriceRepository;
import com.boersenparty.v_1_1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
@EnableScheduling
public class PriceUpdateWorkerService {
    @Value("${webhook_price.callback.url}")
    private String callbackUrl;

    private final CalculatedPriceRepository calculatedPriceRepository;
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate; // For sending webhook updates

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public PriceUpdateWorkerService(CalculatedPriceRepository calculatedPriceRepository,
                                    ProductRepository productRepository,
                                    RestTemplate restTemplate) {
        this.calculatedPriceRepository = calculatedPriceRepository;
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;

    }

    // Once a product is created, this worker starts
    public void startPriceUpdateWorker(Product product) {
        System.out.println("start PriceUpdateWorker called!!!");

        scheduler.scheduleAtFixedRate(() -> {
            try {

                //calculate stock market-like price
                double newPrice = calculatePriceBasedOnCurrent(product);

                CalculatedPrice updatedCalculatedPrice = new CalculatedPrice(product, newPrice);
                System.out.println("updatedCalculated Price is: " + updatedCalculatedPrice);
                calculatedPriceRepository.save(updatedCalculatedPrice);

                // webhook
                triggerPriceUpdateWebhook(updatedCalculatedPrice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 10, TimeUnit.SECONDS);


    }

    private double calculatePriceBasedOnCurrent(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        // latest price
        List<CalculatedPrice> calculatedPrices = calculatedPriceRepository.findByProductId(product.getId());

        double currentPrice = product.getPrice_min(); // Default
        if (calculatedPrices != null && !calculatedPrices.isEmpty()) {
            CalculatedPrice latestCalculatedPrice = calculatedPrices.get(calculatedPrices.size() - 1);
            currentPrice = latestCalculatedPrice.getPrice();
        }

        double baseStepSize = 0.4;
        //based on stock
        if (product.getpQuantity() >= 1 && product.getpQuantity() <= 15) {
            baseStepSize = 1.5;
        } else if (product.getpQuantity() >= 50 && product.getpQuantity() <= 60) {
            baseStepSize = 0.2;
        }

        // more randomness
        double randomStepSize = baseStepSize * (0.5 + Math.random()); // Random multiplier between 0.5 and 1.5

        double randomFactor = Math.random() > 0.5 ? 1 : -1; //  direction
        double randomMagnitude = randomStepSize * (Math.random() * 2);
        double newPrice = currentPrice + randomFactor * randomMagnitude;


        //range
        if (newPrice < product.getPrice_min()) {
            newPrice = product.getPrice_min();
        } else if (newPrice > product.getPrice_max()) {
            newPrice = product.getPrice_max();
        }

        return Math.floor(newPrice * 100) / 100.0;
    }




    // Send the updated price to the webhook
    private void triggerPriceUpdateWebhook(CalculatedPrice calculatedPrice) {
        System.out.println("triggerPriceUpdateWebhook called!");
        String url = callbackUrl + "/" + calculatedPrice.getProduct().getId();
        System.out.println("POSTing to: " + url);
        PriceUpdateEvent priceUpdateEvent = new PriceUpdateEvent(calculatedPrice.getProduct().getId(), calculatedPrice);
        System.out.println("PriceUpdateEvent looks like:" + priceUpdateEvent);
        // Send the updated price to the webhook URL
        restTemplate.postForEntity(url, priceUpdateEvent, Void.class);
    }
}

