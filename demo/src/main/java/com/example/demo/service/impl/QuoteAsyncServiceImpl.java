package com.example.demo.service.impl;

import com.example.demo.dto.feign.QuoteResponse;
import com.example.demo.rest.QuoteClient;
import com.example.demo.service.QuoteAsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__( @Autowired))
public class QuoteAsyncServiceImpl implements QuoteAsyncService {

    private final QuoteClient client;

    @Async
    @Override
    public CompletableFuture<QuoteResponse> ambilQuote() {
        log.info("[{}] mulai ambil quote", Thread.currentThread().getName());
        QuoteResponse quote = client.getQuote();
        log.info("[{}] selesai ambil quote id={}", Thread.currentThread().getName(), quote.getId());
        return CompletableFuture.completedFuture(quote);
    }
}
