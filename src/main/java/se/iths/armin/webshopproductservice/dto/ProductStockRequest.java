package se.iths.armin.webshopproductservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductStockRequest(
        @NotNull(message = "Product ID cannot be null")
        Long productId,
        @Positive(message = "Quantity must be greater than 0")
        int quantity
) {
}
