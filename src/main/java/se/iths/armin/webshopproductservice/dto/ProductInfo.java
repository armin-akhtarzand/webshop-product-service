package se.iths.armin.webshopproductservice.dto;

import java.math.BigDecimal;

/**
 * Innehåller produktinformation vid uppdatering av lagersaldo.
 */
public record ProductInfo(
        Long id,
        String name,
        BigDecimal price,
        int quantity
) {
}
