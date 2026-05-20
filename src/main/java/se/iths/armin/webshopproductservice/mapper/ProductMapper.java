package se.iths.armin.webshopproductservice.mapper;

import se.iths.armin.webshopproductservice.dto.ProductInfo;
import se.iths.armin.webshopproductservice.dto.ProductRequestDto;
import se.iths.armin.webshopproductservice.dto.ProductResponseDto;
import se.iths.armin.webshopproductservice.model.Product;

public interface ProductMapper {
    Product toEntity(ProductRequestDto dto);

    ProductResponseDto toDto(Product product);

    ProductInfo toProductInfo(Product product, int quantity);
}
