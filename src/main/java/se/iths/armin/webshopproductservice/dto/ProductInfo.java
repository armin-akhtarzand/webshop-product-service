package se.iths.armin.webshopproductservice.dto;

import java.math.BigDecimal;

public record ProductInfo(
        Long id,
        String name,
        BigDecimal price,
        int quantity
) {
}
