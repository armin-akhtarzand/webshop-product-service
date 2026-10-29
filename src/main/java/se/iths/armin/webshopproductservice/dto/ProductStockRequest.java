package se.iths.armin.webshopproductservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Innehåller produktens ID och antalet som ska dras från lagret.
 */
public record ProductStockRequest(
        @NotNull(message = "Product ID cannot be null")
        Long productId,
        @Positive(message = "Quantity must be greater than 0")
        int quantity
) {
}
