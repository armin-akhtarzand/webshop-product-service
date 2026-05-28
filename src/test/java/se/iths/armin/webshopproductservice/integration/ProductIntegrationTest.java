package se.iths.armin.webshopproductservice.integration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.iths.armin.webshopproductservice.dto.ProductRequestDto;
import se.iths.armin.webshopproductservice.dto.ProductStockRequest;
import se.iths.armin.webshopproductservice.model.Product;
import se.iths.armin.webshopproductservice.repository.ProductRepository;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product savedProduct;


    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        savedProduct = productRepository.save(
                Product.builder()
                        .name("Laptop")
                        .description("Gaming laptop")
                        .price(BigDecimal.valueOf(15000))
                        .stock(10)
                        .build()
        );
    }

    // ---- CREATE PRODUCT ----

    @Test
    void createProductShouldReturn201ForAdmin() throws Exception {
        var dto = new ProductRequestDto(
                "Samsung Galaxy",
                "Smartphone",
                BigDecimal.valueOf(13000),
                5
        );

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isCreated());
    }


    @Test
    void createProductShouldReturn403ForUser() throws Exception {
        var dto = new ProductRequestDto(
                "iPhone Air",
                "Smartphone",
                BigDecimal.valueOf(13000),
                5
        );

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }


    @Test
    void createProductWithoutTokenShouldReturn401() throws Exception {
        var dto = new ProductRequestDto(
                "Airpods Max",
                "Headphones",
                BigDecimal.valueOf(6000),
                5
        );

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }


    // ---- GET ALL PRODUCTS ----

    @Test
    void getProductsShouldReturn200ForUser() throws Exception {

        mockMvc.perform(get("/products")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    void getProductsShouldReturn200ForAdmin() throws Exception {

        mockMvc.perform(get("/products")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }


    // ---- GET BY ID ----

    @Test
    void getProductByIdShouldReturn200() throws Exception {
        mockMvc.perform(get("/products/" + savedProduct.getId())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }


    // ---- DELETE PRODUCT ----

    @Test
    void deleteProductShouldReturn204ForAdmin() throws Exception {
        mockMvc.perform(delete("/products/" + savedProduct.getId())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProductShouldReturn403ForUser() throws Exception {
        mockMvc.perform(delete("/products/" + savedProduct.getId())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }


    // ---- DECREASE STOCK ----

    @Test
    void decreaseStockShouldReturn200() throws Exception {

        var request = List.of(
                new ProductStockRequest(savedProduct.getId(), 2)
        );

        mockMvc.perform(post("/products/stock/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }


    @Test
    void decreaseStockWithoutTokenShouldReturn401() throws Exception {

        var request = List.of(
                new ProductStockRequest(savedProduct.getId(), 2)
        );

        mockMvc.perform(post("/products/stock/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void decreaseStockShouldReturn400WhenStockIsInsufficient() throws Exception {

        var request = List.of(
                new ProductStockRequest(savedProduct.getId(), 100)
        );

        // försöker minska stock med 100, men bara 10 finns i lager
        mockMvc.perform(post("/products/stock/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isBadRequest());

        var product = productRepository.findById(savedProduct.getId())
                .orElseThrow();

        // verifiera att stock inte ändrats i db
        assertEquals(10, product.getStock());
    }


    @Test
    void decreaseStockShouldReturn404WhenProductNotFound() throws Exception {

        var request = List.of(
                new ProductStockRequest(99999L, 2)
        );
        mockMvc.perform(post("/products/stock/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isNotFound());
    }


    @Test
    void decreaseStockShouldBeAtomicForMultipleProducts() throws Exception {

        var p2 = productRepository.save(
                Product.builder()
                        .name("Mouse")
                        .description("Gaming mouse")
                        .price(BigDecimal.valueOf(500))
                        .stock(5)
                        .build()
        );

        // p2 har ej tillräckligt stock - ska trigga rollback för båda
        var request = List.of(
                new ProductStockRequest(savedProduct.getId(), 2),
                new ProductStockRequest(p2.getId(), 100)
        );

        mockMvc.perform(post("/products/stock/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isBadRequest());

        var updatedProduct1 = productRepository.findById(savedProduct.getId())
                .orElseThrow();
        var updatedProduct2 = productRepository.findById(p2.getId())
                .orElseThrow();

        // verifiera att ingen produkt ändrats i db (rollback)
        assertEquals(10, updatedProduct1.getStock());
        assertEquals(5, updatedProduct2.getStock());
    }


    @Test
    void decreaseStockShouldReturn200ForEmptyList() throws Exception {

        mockMvc.perform(post("/products/stock/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }


}