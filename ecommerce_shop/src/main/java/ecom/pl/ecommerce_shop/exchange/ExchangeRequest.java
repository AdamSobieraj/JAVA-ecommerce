package ecom.pl.ecommerce_shop.exchange;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExchangeRequest {

    @NotNull
    private BigDecimal amount;
    @NotNull
    private Currency currency;
}
