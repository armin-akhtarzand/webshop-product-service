package se.iths.armin.webshopproductservice.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


public record ProductRequestDto(
        @NotBlank(message = "Namn får inte vara tomt")
        String name,
        String description,

        @Positive(message = "Pris måste vara större än 0")
        double price,

        @Min(value = 0, message = "Lager (stock) kan inte vara negativt")
        int stock
) {
}