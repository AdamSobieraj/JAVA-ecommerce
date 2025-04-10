package ecom.pl.ecommerce_shop.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PromotionCodeRepository extends JpaRepository<PromotionCode, UUID> {
}
