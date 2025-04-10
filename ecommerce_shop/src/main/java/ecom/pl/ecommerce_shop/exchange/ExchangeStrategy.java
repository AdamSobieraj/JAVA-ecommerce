package ecom.pl.ecommerce_shop.exchange;

import java.math.BigDecimal;

public interface ExchangeStrategy {
    ExchangeResult execute(BigDecimal amount);
}
