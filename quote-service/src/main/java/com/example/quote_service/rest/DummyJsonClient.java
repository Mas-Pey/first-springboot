package com.example.quote_service.rest;

import com.example.quote_service.feign.QuoteListResponse;
import com.example.quote_service.feign.QuoteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "dummyJsonClient", url = "${dummyjson.url}")
public interface DummyJsonClient {

    @GetMapping("/quotes/random")
    QuoteResponse getQuote();

    @GetMapping("/quotes/{id}")
    QuoteResponse getQuoteById(@PathVariable("id") Long id);

    @GetMapping("/quotes/random/{length}")
    List<QuoteResponse> getRandomQuotes(@PathVariable("length") int length);

    @GetMapping("/quotes")
    QuoteListResponse getQuotes(@RequestParam("limit") int limit, @RequestParam("skip") int skip);
}
