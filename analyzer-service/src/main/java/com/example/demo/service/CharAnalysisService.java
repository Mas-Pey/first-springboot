package com.example.demo.service;

import com.example.demo.dto.request.CharAnalysisRequest;
import com.example.demo.dto.response.CharAnalysisResponse;
import com.example.demo.dto.response.QuotePageAnalysisResponse;

import java.util.List;

public interface CharAnalysisService {
    List<CharAnalysisResponse> cari(String kata);

    CharAnalysisResponse analisis(CharAnalysisRequest request);

    CharAnalysisResponse cariById(Long id);

    List<CharAnalysisResponse> cariMinimalHuruf(int min);

    List<CharAnalysisResponse> limaTerbaru();

    CharAnalysisResponse analisisDariQuote();


    CharAnalysisResponse analysisQuoteById(Long id);

    List<CharAnalysisResponse> analisisBanyakQuote(int jumlah);

    QuotePageAnalysisResponse analisisHalamanQuote(int limit, int skip);

    List<CharAnalysisResponse> analysisDoubleQuote();
}
