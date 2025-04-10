package ecom.pl.ecommerce_shop.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<List<Cart>> findAllByUserId(UUID userId);
}
