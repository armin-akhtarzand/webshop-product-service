package se.iths.armin.webshopproductservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.iths.armin.webshopproductservice.dto.ProductInfo;
import se.iths.armin.webshopproductservice.dto.ProductRequestDto;
import se.iths.armin.webshopproductservice.dto.ProductResponseDto;
import se.iths.armin.webshopproductservice.dto.ProductStockRequest;
import se.iths.armin.webshopproductservice.exception.InsufficientStockException;
import se.iths.armin.webshopproductservice.exception.ProductNotFoundException;
import se.iths.armin.webshopproductservice.mapper.ProductMapper;
import se.iths.armin.webshopproductservice.model.Product;
import se.iths.armin.webshopproductservice.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Hanterar produktlogik vid skapande, hämtning,
 * borttagning och uppdatering av produkters lagersaldo.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Skapar och sparar en ny produkt.
     *
     * @param dto uppgifter för produkten som skapas
     * @return den skapande produkten som en respons DTO
     */
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product saved = productRepository.save(productMapper.toEntity(dto));
        return productMapper.toDto(saved);
    }

    /**
     * Hämtar alla produkter.
     *
     * @return en lista med produkter som respons DTO:er
     */
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    /**
     * Hämtar en produkt med angivet ID.
     *
     * @param id produktens id
     * @return produkten som en respons DTO
     * @throws ProductNotFoundException om produkten inte finns
     */
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toDto(product);
    }

    /**
     * Tar bort en produkt med angivet ID.
     *
     * @param id ID för produkten som ska tas bort
     * @throws ProductNotFoundException om produkten inte finns
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Minskar lagersaldot för de angivna produkterna.
     * Uppdateringen av lagersaldot sker i en och samma transaktion.
     *
     * @param items produkter och antal som ska dras från lagret
     * @return produkterna och antalet som dragits från lagret
     * @throws ProductNotFoundException   om någon produkt inte finns
     * @throws InsufficientStockException om någon produkt har otillräckligt lagersaldo
     */
    @Transactional
    public List<ProductInfo> decreaseStock(List<ProductStockRequest> items) {

        List<ProductInfo> updatedProducts = new ArrayList<>();

        for (ProductStockRequest item : items) {

            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ProductNotFoundException(item.productId()));

            if (product.getStock() < item.quantity()) {
                throw new InsufficientStockException(product.getName());
            }

            product.setStock(product.getStock() - item.quantity());

            ProductInfo info = productMapper.toProductInfo(product, item.quantity());
            updatedProducts.add(info);
        }

        return updatedProducts;
    }


}
