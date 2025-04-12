package ecom.pl.ecommerce_shop.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategory(String category);

    Product findById(UUID id);

    @Query(value = "SELECT * FROM Product WHERE category = :category AND available = true ORDER BY price ASC", nativeQuery = true)
    List<Product> findAvailableProductsByCategoryOrderedByPrice(@Param("category") String category);

    @Query(value = "SELECT DISTINCT category FROM Product", nativeQuery = true)
    List<String> findAllAvailableCategories();

}
