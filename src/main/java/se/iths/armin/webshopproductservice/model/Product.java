package se.iths.armin.webshopproductservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Representerar en produkt i systemet.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private int stock;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String imageUrl;
}
