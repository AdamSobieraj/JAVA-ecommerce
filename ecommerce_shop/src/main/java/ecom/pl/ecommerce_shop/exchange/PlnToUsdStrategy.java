package ecom.pl.ecommerce_shop.exchange;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class PlnToUsdStrategy implements ExchangeStrategy {

    private final ExchangeRateService exchangeRateService;

    @Override
    public ExchangeResult execute(BigDecimal amount) {
        ExchangeRate exchangeRate = exchangeRateService.getLatestExchangeRate(Currency.USD.getCode());
        BigDecimal resultAmount = amount.multiply(BigDecimal.valueOf(exchangeRate.getMid()));

        ExchangeResult result = new ExchangeResult();
        result.setFromCurrency(Currency.PLN.getName());
        result.setToCurrency(Currency.USD.getName());
        result.setAmount(amount);
        result.setResultAmount(resultAmount);
        result.setExchangeRate(BigDecimal.valueOf(exchangeRate.getMid()));

        return result;
    }

}
