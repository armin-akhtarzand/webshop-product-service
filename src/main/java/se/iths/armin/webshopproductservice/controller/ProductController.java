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

/**
 * Hanterar HTTP-anrop för att skapa, hämta och ta bort produkter
 * samt uppdatera produkternas lagersaldo.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Skapar en ny produkt.
     *
     * @param dto uppgifter för produkten som ska skapas
     * @return den skapade produkten med HTTP-status 201
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(
            @Valid @RequestBody ProductRequestDto dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(dto));
    }

    /**
     * Minskar lagersaldot för de angivna produkterna.
     *
     * @param items produkter och antal som ska dras från lagret
     * @return uppdaterad produktinformation med HTTP-status 200
     */
    @PostMapping("/stock/decrease")
    public ResponseEntity<List<ProductInfo>> decreaseStock(
            @Valid @RequestBody List<ProductStockRequest> items) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.decreaseStock(items));
    }

    /**
     * Hämtar alla produkter.
     *
     * @return en lista med produkter och HTTP-status 200
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getAllProducts());
    }

    /**
     * Hämtar en produkt med angivet ID.
     *
     * @param id ID för produkten som ska hämtas
     * @return produkten med HTTP-status 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(
            @PathVariable Long id) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProductById(id));
    }

    /**
     * Tar bort en produkt med angivet ID.
     *
     * @param id ID för produkten som ska tas bort
     * @return ett tomt svar med HTTP-status 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
