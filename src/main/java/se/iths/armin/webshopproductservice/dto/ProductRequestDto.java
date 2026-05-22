package se.iths.armin.webshopproductservice.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;


public record ProductRequestDto(
        @NotBlank(message = "Name cannot be empty")
        String name,
        String description,

        @Positive(message = "Price must be greater than 0")
        BigDecimal price,

        @Min(value = 0, message = "Stock cannot be negative")
        int stock
) {
}