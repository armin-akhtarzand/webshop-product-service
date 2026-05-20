package se.iths.armin.webshopproductservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductStockRequest(
        @NotNull(message = "Produkt-id får inte vara null")
        Long productId,
        @Positive(message = "Antal måste vara större än 0")
        int quantity
) {
}
