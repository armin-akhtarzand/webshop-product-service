package se.iths.armin.webshopproductservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Hanterar undantag och omvandlar dem till HTTP-svar.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Hanterar när en produkt inte hittas.
     *
     * @param ex undantaget som innehåller felmeddelandet
     * @return ett HTTP-svar med statuskod 404
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Hanterar när lagersaldot inte räcker till.
     *
     * @param ex undantaget som innehåller felmeddelandet
     * @return ett HTTP-svar med statuskod 400
     */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> handleInsufficientStockException(InsufficientStockException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Hanterar valideringsfel i inkommande anrop.
     *
     * @param ex undantaget som innehåller information om valideringsfelen
     * @return ett HTTP-svar med statuskod 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .orElse(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    /**
     * Hanterar oväntade undantag.
     *
     * @param ex undantaget som uppstod
     * @return ett HTTP-svar med statuskod 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }


}
