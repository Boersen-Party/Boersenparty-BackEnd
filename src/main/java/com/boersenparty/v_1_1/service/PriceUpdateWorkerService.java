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

    // A method that starts the price calculation worker for a given product
    public void startPriceUpdateWorker(Product product) {
        System.out.println("start PriceUpdateWorker called!!!");

        scheduler.scheduleAtFixedRate(() -> {

            // Fetch the tail element (last calculated price) from the calculatedPrices list
            List<CalculatedPrice> calculatedPrices = product.getCalculatedPrices();
            System.out.println("caluclated prices list:" + calculatedPrices);
            CalculatedPrice calculatedPrice = null;

            if (calculatedPrices != null && !calculatedPrices.isEmpty()) {
                calculatedPrice = calculatedPrices.get(calculatedPrices.size() - 1);
            }
            // Calculate the new price based on the current price
            double newPrice = calculatePriceBasedOnCurrent(calculatedPrice);

            // Save the updated price with the current time
            CalculatedPrice updatedCalculatedPrice = new CalculatedPrice(product, newPrice);
            System.out.println("updatedCalculated Price is: " + updatedCalculatedPrice);
            //calculatedPriceRepository.save(updatedCalculatedPrice);
            System.out.println("calculated price looks like this in db:");
            System.out.println(calculatedPriceRepository.save(updatedCalculatedPrice));


            // Send the updated price to the webhook callback URL
            triggerPriceUpdateWebhook(updatedCalculatedPrice);
        }, 0, 10, TimeUnit.SECONDS);  // Adjust the schedule as needed (1 hour interval)


    }

    // Calculate the new price based on the current price and price range
    private double calculatePriceBasedOnCurrent(CalculatedPrice calculatedPrice) {
        double currentPrice = calculatedPrice.getPrice();
        Product product = calculatedPrice.getProduct();

        double priceAdjustmentFactor = 0.05;
        double priceRangeFactor = (product.getPrice_max() - product.getPrice_min()) * priceAdjustmentFactor;
        double newPrice = currentPrice + priceRangeFactor;

        if (newPrice < product.getPrice_min()) {
            newPrice = product.getPrice_min();
        } else if (newPrice > product.getPrice_max()) {
            newPrice = product.getPrice_max();
        }

        return newPrice;
    }

    // Send the updated price to the webhook (using the price callback URL)
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

