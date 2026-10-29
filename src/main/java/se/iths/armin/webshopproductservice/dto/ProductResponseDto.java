package se.iths.armin.webshopproductservice.dto;

import se.iths.armin.webshopproductservice.model.Category;

import java.math.BigDecimal;

/**
 * Innehåller produktinformation som returneras från API:et.
 */
public record ProductResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        Category category,
        String imageUrl
) {
}
