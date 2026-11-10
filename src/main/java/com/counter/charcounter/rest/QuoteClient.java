package com.counter.charcounter.rest;

import com.counter.charcounter.dto.feign.QuoteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "quoteClient", url = "${quote.api.url}")
public interface QuoteClient {
    @GetMapping("/quotes/random")
    QuoteResponse getQuote();

    @GetMapping("/quotes/{id}")
    QuoteResponse quoteById(@PathVariable("id") Long id);
}
