package com.example.demo.controller;

import com.example.demo.dto.request.CharAnalysisRequest;
import com.example.demo.dto.response.BaseResponse;
import com.example.demo.dto.response.CharAnalysisResponse;
import com.example.demo.dto.response.QuotePageAnalysisResponse;
import com.example.demo.service.CharAnalysisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/analyzer")
@RequiredArgsConstructor(onConstructor = @__( @Autowired))
public class AnalyzerController {

    private final CharAnalysisService charAnalysisService;

    @GetMapping
    public BaseResponse<List<CharAnalysisResponse>> cari(
            @RequestParam(required = false) String kalimat) {
        return BaseResponse.ok(charAnalysisService.cari(kalimat));
    }

    @PostMapping
    public BaseResponse<CharAnalysisResponse> analisis(
            @Valid
            @RequestBody CharAnalysisRequest request,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {
        log.info("X-Request-Id: {}, kalimat: {}", requestId, request.getKalimat());
        return BaseResponse.ok(charAnalysisService.analisis(request));
    }

    @GetMapping("/{id}")
    public BaseResponse<CharAnalysisResponse> detail(@PathVariable Long id) {
        CharAnalysisResponse hasil = charAnalysisService.cariById(id);
        return BaseResponse.ok(hasil);
    }

    @GetMapping("/minimal")
    public BaseResponse<List<CharAnalysisResponse>> minimal(@RequestParam int min) {
        return BaseResponse.ok(charAnalysisService.cariMinimalHuruf(min));
    }

    @GetMapping("/terbaru")
    public BaseResponse<List<CharAnalysisResponse>> terbaru() {
        return BaseResponse.ok(charAnalysisService.limaTerbaru());
    }

    @PostMapping("/from-quote")
    public BaseResponse<CharAnalysisResponse> fromQuote() {
        return BaseResponse.ok(charAnalysisService.analisisDariQuote());
    }

    @PostMapping("/from-quote/{id}")
    public BaseResponse<CharAnalysisResponse> fromQuoteById(@PathVariable Long id) {
        return BaseResponse.ok(charAnalysisService.analysisQuoteById(id));
    }

    @PostMapping("/from-quote/batch/{jumlah}")
    public BaseResponse<List<CharAnalysisResponse>> fromBanyakQuote(@PathVariable int jumlah) {
        return BaseResponse.ok(charAnalysisService.analisisBanyakQuote(jumlah));
    }

    @PostMapping("/from-quote/page")
    public BaseResponse<QuotePageAnalysisResponse> fromHalamanQuote(
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "0") int skip) {
        return BaseResponse.ok(charAnalysisService.analisisHalamanQuote(limit, skip));
    }

    @PostMapping("/from-qoute/double")
    public BaseResponse<List<CharAnalysisResponse>> fromDoubleQuote() {
        return BaseResponse.ok(charAnalysisService.analysisDoubleQuote());
    }
}
