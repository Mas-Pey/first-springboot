package com.example.demo.service;

import com.example.demo.dto.feign.QuoteResponse;

import java.util.concurrent.CompletableFuture;

public interface QuoteAsyncService {
    CompletableFuture<QuoteResponse> getQuote();
}
