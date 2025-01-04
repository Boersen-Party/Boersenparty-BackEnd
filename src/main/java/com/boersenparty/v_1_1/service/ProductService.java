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
import jakarta.persistence.EntityNotFoundException;
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
        this.priceUpdateWorkerService.startPriceUpdateWorker(savedProduct.getId());

        return ProductDTOMapper.toDTO(savedProduct);
    }


    public void deleteProduct(Long party_id, Long product_id) {
        Party party = partyRepository.findById(party_id)
                .orElseThrow(() -> new IllegalArgumentException("Party not found with ID: " + party_id));

        Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + product_id));

        if (!product.getParty().equals(party)) {
            throw new IllegalArgumentException("Product does not belong to this party");
        }

        productRepository.delete(product);
    }

    public ProductDTO updateProduct(ProductDTO productDTO, Long partyId, Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new EntityNotFoundException("Party not found with ID: " + partyId));

        if (!existingProduct.getParty().getId().equals(partyId)) {
            throw new IllegalArgumentException("Product does not belong to the specified party.");
        }

        existingProduct.setName(productDTO.getName());
        existingProduct.setProductType(productDTO.getProductType());
        existingProduct.setPrice_max(productDTO.getPrice_max());
        existingProduct.setPrice_min(productDTO.getPrice_min());

        //initial/base price will explicitly not be altered with a post
        existingProduct.setpQuantity(productDTO.getpQuantity());
        existingProduct.setImageURL(productDTO.getImageURL());

        Product savedProduct = productRepository.save(existingProduct);

        return ProductDTOMapper.toDTO(savedProduct);
    }
}
