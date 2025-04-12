package ecom.pl.ecommerce_shop.exchange;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ExchangeResult {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
    private BigDecimal resultAmount;
    private BigDecimal exchangeRate;

}
