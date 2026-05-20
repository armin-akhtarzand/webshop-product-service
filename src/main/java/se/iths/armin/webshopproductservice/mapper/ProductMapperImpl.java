package se.iths.armin.webshopproductservice.mapper;

import org.springframework.stereotype.Component;
import se.iths.armin.webshopproductservice.dto.ProductInfo;
import se.iths.armin.webshopproductservice.dto.ProductRequestDto;
import se.iths.armin.webshopproductservice.dto.ProductResponseDto;
import se.iths.armin.webshopproductservice.model.Product;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        return product;
    }

    @Override
    public ProductResponseDto toDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }

    @Override
    public ProductInfo toProductInfo(Product product, int quantity) {
        return new ProductInfo(
                product.getId(),
                product.getName(),
                product.getPrice(),
                quantity
        );
    }


}
