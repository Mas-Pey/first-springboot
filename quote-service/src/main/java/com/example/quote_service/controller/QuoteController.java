package com.example.quote_service.controller;

import com.example.quote_service.feign.QuoteListResponse;
import com.example.quote_service.feign.QuoteResponse;
import com.example.quote_service.rest.DummyJsonClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/quotes")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuoteController {

    private final DummyJsonClient dummyJsonClient;

    @Value("${server.port}")
    private String port;

    @GetMapping("/random")
    public QuoteResponse randomQuote() {
        log.info("Testing random quote");
        return dummyJsonClient.getQuote();
    }

    @GetMapping("/{id}")
    public QuoteResponse byId(@PathVariable Long id) {
        log.info("Testing by id {}", id);
        return dummyJsonClient.getQuoteById(id);
    }

    @GetMapping("/random/{length}")
    public List<QuoteResponse> randomLength(@PathVariable int length) {
        return dummyJsonClient.getRandomQuotes(length);
    }

    @GetMapping
    public QuoteListResponse daftar(@RequestParam("limit") int limit, @RequestParam("skip") int skip) {
        return dummyJsonClient.getQuotes(limit, skip);
    }
}
