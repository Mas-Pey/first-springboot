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

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuoteAsyncServiceImpl implements QuoteAsyncService {

    private final QuoteClient quoteClient;

    @Async
    @Override
    public CompletableFuture<QuoteResponse> getQuote() {
        log.info("[{}] mulai catat riwayat]", Thread.currentThread().getName());
        QuoteResponse quoteResponse = quoteClient.getQuote();
        log.info("[{}] selesai catat riwayat id={}]", Thread.currentThread().getName(), quoteResponse.getId());
        return CompletableFuture.completedFuture(quoteResponse);
    }
}
