package ecom.pl.ecommerce_shop.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
public class ExchangeRateTable {

    @JsonProperty("table")
    private String table;

    @JsonProperty("no")
    private String no;

    @JsonProperty("effectiveDate")
    private String effectiveDate;

    @JsonProperty("rates")
    private List<ExchangeRate> rates;

}