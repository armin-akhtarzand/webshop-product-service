package se.iths.armin.webshopproductservice.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import se.iths.armin.webshopproductservice.model.Category;

import java.math.BigDecimal;


public record ProductRequestDto(
        @NotBlank(message = "Name cannot be empty")
        String name,
        String description,

        @Positive(message = "Price must be greater than 0")
        BigDecimal price,

        @Min(value = 0, message = "Stock cannot be negative")
        int stock,

        @NotNull(message = "Category cannot be null")
        Category category,

        String imageUrl

) {
}