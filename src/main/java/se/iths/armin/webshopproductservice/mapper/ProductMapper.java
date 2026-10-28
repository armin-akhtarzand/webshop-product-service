package se.iths.armin.webshopproductservice.mapper;

import se.iths.armin.webshopproductservice.dto.ProductInfo;
import se.iths.armin.webshopproductservice.dto.ProductRequestDto;
import se.iths.armin.webshopproductservice.dto.ProductResponseDto;
import se.iths.armin.webshopproductservice.model.Product;

/**
 * Definierar mappning mellan produktentiteter och DTO:er.
 */
public interface ProductMapper {

    /**
     * Omvandlar en produktbegäran till en produktentitet.
     *
     * @param dto uppgifter för produkten
     * @return en produktentitet
     */
    Product toEntity(ProductRequestDto dto);

    /**
     * Omvandlar en produktentitet till ett produktsvars-DTO.
     *
     * @param product produkten som ska omvandlas
     * @return en svars-DTO med produktens information
     */
    ProductResponseDto toDto(Product product);

    /**
     * Skapar produktinformation för en lageruppdatering.
     *
     * @param product  produkten som informationen gäller
     * @param quantity antalet produkter som dras från lagret
     * @return produktinformation och det angivna antalet
     */
    ProductInfo toProductInfo(Product product, int quantity);
}
