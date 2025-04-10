package ecom.pl.ecommerce_shop.exchange;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class UsdToPlnStrategy implements ExchangeStrategy {

    private final ExchangeRateService exchangeRateService;

    @Override
    public ExchangeResult execute(BigDecimal amount) {
        ExchangeRate exchangeRate = exchangeRateService.getLatestExchangeRate(Currency.USD.getCode());
        BigDecimal resultAmount = amount.divide(BigDecimal.valueOf(exchangeRate.getMid()), 4, RoundingMode.HALF_UP);

        ExchangeResult result = new ExchangeResult();
        result.setFromCurrency(Currency.USD.getName());
        result.setToCurrency(Currency.PLN.getName());
        result.setAmount(amount);
        result.setResultAmount(resultAmount);
        result.setExchangeRate(BigDecimal.valueOf(exchangeRate.getMid()));

        return result;
    }

}
