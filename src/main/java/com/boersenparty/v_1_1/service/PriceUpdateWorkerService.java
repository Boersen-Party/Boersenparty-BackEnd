package com.boersenparty.v_1_1.service;
import com.boersenparty.v_1_1.events.PriceUpdateEvent;
import com.boersenparty.v_1_1.models.CalculatedPrice;
import com.boersenparty.v_1_1.models.Product;
import com.boersenparty.v_1_1.models.Event;
import com.boersenparty.v_1_1.repository.CalculatedPriceRepository;
import com.boersenparty.v_1_1.repository.EventRepository;
import com.boersenparty.v_1_1.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


@Service
@EnableScheduling
public class PriceUpdateWorkerService {
    @PersistenceContext
    private EntityManager entityManager;
    @Value("${webhook_price.callback.url}")
    private String callbackUrl;

    private final CalculatedPriceRepository calculatedPriceRepository;
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<Long, ScheduledFuture<?>> productTasks = new ConcurrentHashMap<>();


    public PriceUpdateWorkerService(CalculatedPriceRepository calculatedPriceRepository,
                                    ProductRepository productRepository, EventRepository eventRepository,
                                    RestTemplate restTemplate) {
        this.calculatedPriceRepository = calculatedPriceRepository;
        this.productRepository = productRepository;
        this.eventRepository = eventRepository;
        this.restTemplate = restTemplate;

    }

    // Once a product is created, this worker starts
    public void startPriceUpdateWorker(Long productId) {
        //dient dazu, dass der UpdateWorker beendet wird, wenn das bearbeitete Produkt gelöscht wird
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
                double newPrice = calculatePriceBasedOnCurrent(product);
                CalculatedPrice updatedCalculatedPrice = new CalculatedPrice(product, newPrice);
                calculatedPriceRepository.save(updatedCalculatedPrice);
                //  webhook
                triggerPriceUpdateWebhook(updatedCalculatedPrice); //hier wird ein externes System benachrichtigt
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                stopPriceUpdateWorker(productId); // wenn Produkt gelöscht
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 10, TimeUnit.SECONDS);
        // für das terminieren von Workern
        productTasks.put(productId, future);
    }

    public void stopPriceUpdateWorker(Long productId) {
        ScheduledFuture<?> future = productTasks.remove(productId);
        if (future != null) {
            future.cancel(true); // Cancel the task
            System.out.println("Stopped price update worker for product ID: " + productId);
        }
    }


    private double calculatePriceBasedOnCurrent(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        List<CalculatedPrice> calculatedPrices = calculatedPriceRepository.findByProductId(product.getId());
        //holt sich den aktuellsten Preis - wenn nicht vorhanden, nutze price_min
        double currentPrice = product.getPrice_min();
        if (calculatedPrices != null && !calculatedPrices.isEmpty()) {
            CalculatedPrice latestCalculatedPrice = calculatedPrices.get(calculatedPrices.size() - 1);
            currentPrice = latestCalculatedPrice.getPrice();
        }
        double baseStepSize = 0.4;
        //Preis "Schritt" basierend auf Bestand - Knappheit eines Produkts macht die StepSize höher
        if (product.getpQuantity() >= 1 && product.getpQuantity() <= 15) {
            baseStepSize = 1.5;
        } else if (product.getpQuantity() >= 50 && product.getpQuantity() <= 60) {
            baseStepSize = 0.2;}
        // mehr Zufall in der Preisberechnung
        double randomStepSize = baseStepSize * (0.5 + Math.random());
        double randomFactor = Math.random() > 0.5 ? 1 : -1; //  "Richtung"
        double randomMagnitude = randomStepSize * (Math.random() * 2);
        double newPrice = currentPrice + randomFactor * randomMagnitude;
        // dient dazu, nicht bei "price_min" fest zu stecken
        double tolerance = 0.01;
        if (newPrice <= product.getPrice_min() + tolerance) {
            newPrice = product.getPrice_min() + tolerance;
        } else if (newPrice >= product.getPrice_max() - tolerance) {
            newPrice = product.getPrice_max() - tolerance;
        }
        return Math.ceil(newPrice * 100) / 100.0;} //aufrunden auf 2. Nach-Kommastellen




    // Updated price werden hier geschickt
    private void triggerPriceUpdateWebhook(CalculatedPrice calculatedPrice) {
        String url = callbackUrl + "/" + calculatedPrice.getProduct().getId();
        PriceUpdateEvent priceUpdateEvent = new PriceUpdateEvent(calculatedPrice.getProduct().getId(), calculatedPrice);
        restTemplate.postForEntity(url, priceUpdateEvent, Void.class);

        String url_webhook = "https://webhook.site/399db4ed-b96c-4a6e-b24a-40e509726c28";
        restTemplate.postForEntity(url_webhook, priceUpdateEvent, Void.class);
    }

    public void startTriggeredEventWorker(Event event) {
        System.out.println("Applying triggered event: " + event.getType());
        List<Product> products = productRepository.findAllByPartyId(event.getParty().getId());

        for (Product product : products) {
            stopPriceUpdateWorker(product.getId()); // stoppt generischen Preis-Worker
            ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
                try {
                    double adjustedPrice = calculatePriceBasedOnCurrent(product);
                    if ("Boersencrash".equalsIgnoreCase(event.getType())) {
                        adjustedPrice *= 0.5;
                    } else if ("Happy Hour".equalsIgnoreCase(event.getType()) &&
                            "mit Alkohol".equalsIgnoreCase(product.getProductType())) {
                        adjustedPrice *= 0.7;
                    }
                    CalculatedPrice calculatedPrice = new CalculatedPrice(product, adjustedPrice);
                    calculatedPriceRepository.save(calculatedPrice);
                    // Trigger webhook
                    triggerPriceUpdateWebhook(calculatedPrice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 0, 10, TimeUnit.SECONDS);

            productTasks.put(product.getId(), future);
        }

        scheduler.schedule(() -> {
            System.out.println("Event duration ended. Resuming normal price updates...");

            for (Product product : products) {
                ScheduledFuture<?> future = productTasks.get(product.getId());
                if (future != null) {
                    future.cancel(true); // Cancel the worker
                    productTasks.remove(product.getId());
                }
            }

            List<Event> allEvents = eventRepository.findAll();
            for (Event _event : allEvents) {
                _event.setIs_ongoing(false);
                _event.setStartsAt(null);
                _event.setEndsAt(null);
            }
            eventRepository.saveAll(allEvents);

            // Restart workers for all products
            List<Product> allProducts = productRepository.findAll();
            allProducts.forEach(product -> startPriceUpdateWorker(product.getId()));

        }, event.getDuration(), TimeUnit.MINUTES);
    }


}



