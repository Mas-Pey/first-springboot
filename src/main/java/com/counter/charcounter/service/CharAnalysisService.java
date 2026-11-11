package com.counter.charcounter.service;

import com.counter.charcounter.dto.request.CharAnalysisRequest;
import com.counter.charcounter.dto.response.CharAnalysisResponse;

import java.util.List;

public interface CharAnalysisService {
    List<CharAnalysisResponse> cari(String kata);
    CharAnalysisResponse simpan(CharAnalysisRequest request);
    CharAnalysisResponse findById(Long id);
    List<CharAnalysisResponse> findWithMinimal(int min);
    List<CharAnalysisResponse> findNew();

    // EXTERNAL API / REST
    CharAnalysisResponse analyzeFromQuote();
    CharAnalysisResponse analyzeFromQuoteById(Long id);
    List<CharAnalysisResponse> analyzeFromRandomQuoteBatch(int length);
    List<CharAnalysisResponse> analyzeFromQuotePagination(int limit, int skip);

    // ASYNC API
    List<CharAnalysisResponse> analyzeDoubleQuote();
}
