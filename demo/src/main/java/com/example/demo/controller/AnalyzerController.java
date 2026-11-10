package com.example.demo.controller;

import com.example.demo.dto.request.CharAnalysisRequest;
import com.example.demo.dto.response.BaseResponse;
import com.example.demo.dto.response.CharAnalysisResponse;
import com.example.demo.service.CharAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
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
            @RequestBody CharAnalysisRequest request,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {
        log.info("X-Request-Id: {}, kalimat: {}", requestId, request.getKalimat());
        return BaseResponse.ok(charAnalysisService.analisis(request));
    }

    @GetMapping("/{id}")
    public BaseResponse<CharAnalysisResponse> detail(@PathVariable Long id) {
        CharAnalysisResponse hasil = charAnalysisService.cariById(id);
        if (hasil == null) {
            return BaseResponse.error("Data dengan id " + id + " tidak ditemukan");
        }
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
}
