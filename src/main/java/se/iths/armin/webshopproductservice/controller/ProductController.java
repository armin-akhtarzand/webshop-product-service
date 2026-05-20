package se.iths.armin.webshopproductservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.iths.armin.webshopproductservice.dto.ProductInfo;
import se.iths.armin.webshopproductservice.dto.ProductRequestDto;
import se.iths.armin.webshopproductservice.dto.ProductResponseDto;
import se.iths.armin.webshopproductservice.dto.ProductStockRequest;
import se.iths.armin.webshopproductservice.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(
            @Valid @RequestBody ProductRequestDto dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(dto));
    }

    @PostMapping("/stock/decrease")
    public ResponseEntity<List<ProductInfo>> decreaseStock(
            @Valid @RequestBody List<ProductStockRequest> items) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.decreaseStock(items));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(
            @PathVariable Long id) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProductById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
