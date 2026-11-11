package com.counter.charcounter.service;

import com.counter.charcounter.dto.feign.QuoteResponse;

import java.util.concurrent.CompletableFuture;

public interface QuoteAsyncService {
    CompletableFuture<QuoteResponse> getQuote();
}
