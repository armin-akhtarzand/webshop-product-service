package se.iths.armin.webshopproductservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.armin.webshopproductservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
