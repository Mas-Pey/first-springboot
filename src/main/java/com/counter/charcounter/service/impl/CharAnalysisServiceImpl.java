package com.counter.charcounter.service.impl;

import com.counter.charcounter.dto.feign.QuoteResponse;
import com.counter.charcounter.dto.request.CharAnalysisRequest;
import com.counter.charcounter.dto.response.CharAnalysisResponse;
import com.counter.charcounter.entity.CharAnalysis;
import com.counter.charcounter.exception.AlreadyExistException;
import com.counter.charcounter.exception.AnalysisNotFoundException;
import com.counter.charcounter.exception.QuoteUnavailableException;
import com.counter.charcounter.repository.CharAnalysisRepository;
import com.counter.charcounter.rest.QuoteClient;
import com.counter.charcounter.service.CharAnalysisService;
import com.counter.charcounter.util.CharCounter;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.http.converter.autoconfigure.ClientHttpMessageConvertersCustomizer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CharAnalysisServiceImpl implements CharAnalysisService {

    private final CharAnalysisRepository charAnalysisRepository;
    private final ClientHttpMessageConvertersCustomizer clientConvertersCustomizer;

    private CharAnalysisResponse toResponse (CharAnalysis data) {
        CharCounter counter = new CharCounter(data.getKalimat());
        return CharAnalysisResponse.from(data, counter.toMap());
    }
    private final QuoteClient quoteClient;

    @Override
    public List<CharAnalysisResponse> cari(String kata) {
        List<CharAnalysis> semua;
        if (kata == null || kata.isBlank()) {
            semua = charAnalysisRepository.findAll();
        } else {
            semua = charAnalysisRepository.findByKalimatContainingIgnoreCase(kata);
        }

        List<CharAnalysisResponse> hasil = new ArrayList<>();
        for (CharAnalysis data: semua){
            hasil.add(toResponse(data));
        }
        return hasil;
    }

    @Override
    public CharAnalysisResponse simpan(CharAnalysisRequest request) {
        return simpanData(request.getKalimat(), null);
    }

    @Override
    public CharAnalysisResponse findById(Long id) {
        CharAnalysis data = charAnalysisRepository.findById(id)
                .orElseThrow(() -> new AnalysisNotFoundException(id));
        return toResponse(data);
    }

    @Override
    public List<CharAnalysisResponse> findWithMinimal(int min) {
        List<CharAnalysis> data = charAnalysisRepository.findbyMinimalHuruf(min);
        List<CharAnalysisResponse> result = new ArrayList<>();
        for (CharAnalysis d : data) {
            result.add(toResponse(d));
        }
        return result;
    }

    @Override
    public List<CharAnalysisResponse> findNew() {
        List<CharAnalysis> data = charAnalysisRepository.findNew();
        List<CharAnalysisResponse> result = new ArrayList<>();
        for (CharAnalysis d : data) {
            result.add(toResponse(d));
        }
        return result;
    }

    @Override
    public CharAnalysisResponse analyzeFromQuote() {
        QuoteResponse quote;
        try {
            quote = quoteClient.getQuote();
        } catch (FeignException feignException) {
            throw new QuoteUnavailableException();
        }
        return simpanData(quote.getQuote(), quote.getAuthor());
    }

    private CharAnalysisResponse simpanData(String kalimat, String author) {
        kalimat = kalimat.trim();
        boolean isAlready = charAnalysisRepository.existsByKalimatIgnoreCase(kalimat);
        if (isAlready) {
            throw new AlreadyExistException(kalimat);
        }

        CharCounter counter = new CharCounter(kalimat);
        CharAnalysis data = new CharAnalysis();

        data.setKalimat(kalimat);
        data.setAuthor(author);
        data.setTotalHuruf(counter.getTotalHuruf());
        data.setCreatedAt(LocalDateTime.now());

        CharAnalysis saved = charAnalysisRepository.save(data);

        return CharAnalysisResponse.from(saved, counter.toMap());
    }
}
