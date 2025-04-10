package ecom.pl.ecommerce_shop.exchange;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Validated
@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class  ExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @PostMapping("/exchange")
    public ResponseEntity<ExchangeResult> exchange(@Valid @RequestBody ExchangeRequest request) throws IOException {
        ExchangeResult result = currencyExchangeService.exchange(request);
        return ResponseEntity.ok(result);
    }


}
