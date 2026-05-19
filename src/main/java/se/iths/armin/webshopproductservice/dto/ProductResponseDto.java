package se.iths.armin.webshopproductservice.dto;

public record ProductResponseDto(
        Long id,
        String name,
        String description,
        double price,
        int stock
) {
}
