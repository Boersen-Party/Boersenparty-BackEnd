package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.dto.ProductDTO;
import com.boersenparty.v_1_1.events.PriceUpdateEvent;
import com.boersenparty.v_1_1.mapper.ProductDTOMapper;
import com.boersenparty.v_1_1.models.CalculatedPrice;
import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.models.Product;
import com.boersenparty.v_1_1.repository.CalculatedPriceRepository;
import com.boersenparty.v_1_1.repository.PartyRepository;
import com.boersenparty.v_1_1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductService {


    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final CalculatedPriceRepository calculatedPriceRepository;

    @Autowired
    private final PartyRepository partyRepository;

    @Autowired
    private final PriceUpdateWorkerService priceUpdateWorkerService;

    public ProductService(ProductRepository productRepository,
                          PartyRepository partyRepository,
                          CalculatedPriceRepository calculatedPriceRepository,
                          PriceUpdateWorkerService priceUpdateWorkerService) {

        this.productRepository = productRepository;
        this.partyRepository =  partyRepository;
        this.calculatedPriceRepository = calculatedPriceRepository;
        this.priceUpdateWorkerService = priceUpdateWorkerService;
    }

    public List<ProductDTO> getProducts(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("Party not found with ID: " + partyId));

        return party.getProducts().stream()
                .map(product -> {
                    ProductDTO dto = ProductDTOMapper.toDTO(product);

                    if (product.getCalculatedPrices() != null) {
                        if (!product.getCalculatedPrices().isEmpty()) {
                            CalculatedPrice latestPrice = product.getCalculatedPrices()
                                    .get(product.getCalculatedPrices().size() - 1);
                            dto.setLatestCalculatedPrice(latestPrice.getPrice());
                        }
                    }

                    return dto;
                })
                .toList();
    }



    public ProductDTO createProduct(ProductDTO productDTO, Long partyId) {
        System.out.println("Incoming ProductDTO: " + productDTO);


        // Find the party by ID
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("Party not found with ID: " + partyId));


        Product product = ProductDTOMapper.toEntity(productDTO);
        System.out.println("Mapping DTO into Product object:" + product);
        product.setParty(party);




        Product savedProduct = productRepository.save(product);

        //maybe other way around
        CalculatedPrice initialCalculatedPrice = new CalculatedPrice();
        initialCalculatedPrice.setPrice(productDTO.getLatestCalculatedPrice());
        initialCalculatedPrice.setProduct(product);
        System.out.println("SAVING CALUCLATED PRICEEEE:" + calculatedPriceRepository.save(initialCalculatedPrice));


        //at the very start, the base_price is recieved from the client request, the generated price worker
        //then works with that price, and periodically sends updates

        //id 1 created
        System.out.println("Calculated price id 1 is created (because product created per request) :");
        System.out.println("all entries in Calculated Price:" + calculatedPriceRepository.findAll());



        System.out.println("calling startPriceUpdateWorker... with product:" + savedProduct);
        //triggers webhook 'Product Created'
        this.priceUpdateWorkerService.startPriceUpdateWorker(savedProduct);

        return ProductDTOMapper.toDTO(savedProduct);
    }


}
