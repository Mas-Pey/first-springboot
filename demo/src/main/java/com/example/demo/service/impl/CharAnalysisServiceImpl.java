package com.example.demo.service.impl;

import com.example.demo.dto.feign.QuoteResponse;
import com.example.demo.dto.request.CharAnalysisRequest;
import com.example.demo.dto.response.CharAnalysisResponse;
import com.example.demo.entity.CharAnalysis;
import com.example.demo.exception.AnalysisAlreadyExistException;
import com.example.demo.exception.AnalysisNotFoundException;
import com.example.demo.exception.QuoteUnavailableException;
import com.example.demo.repository.CharAnalysisRepository;
import com.example.demo.rest.QuoteClient;
import com.example.demo.service.CharAnalysisService;
import com.example.demo.service.QuoteAsyncService;
import com.example.demo.service.RiwayatService;
import com.example.demo.util.CharCounter;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__( @Autowired))
public class CharAnalysisServiceImpl implements CharAnalysisService {

    private final QuoteClient client;
    private final CharAnalysisRepository charAnalysisRepository;
    private final RiwayatService riwayatService;
    private final QuoteAsyncService quoteAsyncService;

    @Override
    public List<CharAnalysisResponse> cari(String kata) {
        List<CharAnalysis> semua;
        if (kata == null || kata.isBlank()) {
            semua = charAnalysisRepository.findAll();
        } else {
            semua = charAnalysisRepository.findByKalimatContainingIgnoreCase(kata);
        }

        List<CharAnalysisResponse> hasil = new ArrayList<>();
        for (CharAnalysis data: semua) {
            hasil.add(toResponse(data));
        }
        return hasil;
    }

    @Override
    public CharAnalysisResponse analisis(CharAnalysisRequest request) {
        String kalimat = request.getKalimat().trim();
        if (charAnalysisRepository.existsByKalimatIgnoreCase(kalimat)) {
            throw new AnalysisAlreadyExistException(kalimat);
        }

        return simpan(kalimat);
    }

    @Override
    public CharAnalysisResponse cariById(Long id) {
        CharAnalysis data = charAnalysisRepository.findById(id)
                .orElseThrow(() -> new AnalysisNotFoundException(id));
        return toResponse(data);
    }

    @Override
    public List<CharAnalysisResponse> cariMinimalHuruf(int min) {
        return toResponseList(charAnalysisRepository.cariMinimalHuruf(min));
    }

    @Override
    public List<CharAnalysisResponse> limaTerbaru() {
        return toResponseList(charAnalysisRepository.limaTerbaru());
    }

    @Override
    public CharAnalysisResponse analisisDariQuote() {
        QuoteResponse quote;
        try {
            quote = client.getQuote();
        } catch (FeignException feignException) {
            throw new QuoteUnavailableException("Service unavailable");
        }
        return simpan(quote.getQuote());
    }

    @Override
    public List<CharAnalysisResponse> analisisDuaQuote() {
        long mulai = System.currentTimeMillis();

        // dua request ke DummyJSON dikirim bersamaan, tidak saling menunggu
        CompletableFuture<QuoteResponse> quote1 = quoteAsyncService.ambilQuote();
        CompletableFuture<QuoteResponse> quote2 = quoteAsyncService.ambilQuote();

        List<CharAnalysisResponse> hasil = new ArrayList<>();
        try {
            // join() = tunggu sampai hasilnya datang
            hasil.add(simpan(quote1.join().getQuote()));
            hasil.add(simpan(quote2.join().getQuote()));
        } catch (CompletionException e) {
            throw new QuoteUnavailableException("Service unavailable");
        }

        log.info("[{}] dua quote selesai dalam {} ms",
                Thread.currentThread().getName(), System.currentTimeMillis() - mulai);
        return hasil;
    }

    private CharAnalysisResponse simpan(String kalimat) {
        CharCounter counter = new CharCounter(kalimat);
        CharAnalysis data = new CharAnalysis();

        data.setKalimat(kalimat);
        data.setTotalHuruf(counter.getTotalHuruf());
        data.setCreatedAt(LocalDateTime.now());
        charAnalysisRepository.save(data);

        riwayatService.catatRiwayat(data.getId(), kalimat);   // @Async: tidak ditunggu
        log.info("[{}] response dikirim ke client", Thread.currentThread().getName());

        return CharAnalysisResponse.from(data, counter.toMap());
    }

    private List<CharAnalysisResponse> toResponseList(List<CharAnalysis> semua) {
        List<CharAnalysisResponse> hasil = new ArrayList<>();
        for (CharAnalysis data : semua) {
            hasil.add(toResponse(data));
        }
        return hasil;
    }

    private CharAnalysisResponse toResponse(CharAnalysis data) {
        CharCounter counter = new CharCounter(data.getKalimat());
        return CharAnalysisResponse.from(data, counter.toMap());
    }
}
