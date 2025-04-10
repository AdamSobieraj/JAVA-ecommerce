package ecom.pl.ecommerce_shop.exchange;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRequest {

    @NotNull
    private BigDecimal amount;
    @NotNull
    private Currency currency;
}
