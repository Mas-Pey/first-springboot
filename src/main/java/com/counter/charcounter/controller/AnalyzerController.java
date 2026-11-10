package com.counter.charcounter.controller;

import com.counter.charcounter.dto.feign.QuoteResponse;
import com.counter.charcounter.dto.request.CharAnalysisRequest;
import com.counter.charcounter.dto.response.BaseResponse;
import com.counter.charcounter.dto.response.CharAnalysisResponse;
import com.counter.charcounter.service.CharAnalysisService;
import com.counter.charcounter.util.CharCounter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// Rest : HTTP, berbeda dengan only @Controller: output html
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnalyzerController {

    private final JdbcTemplate jdbcTemplate;
    private final CharAnalysisService charAnalysisService;

    // DI
//    public AnalyzerController(JdbcTemplate jdbcTemplate, CharAnalysisService charAnalysisService) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.charAnalysisService = charAnalysisService;
//    }

    @GetMapping("/db-check")
    public String checkDbConnection() {
        try {
            // Eksekusi query ringan ke PostgreSQL
            String dbName = jdbcTemplate.queryForObject("SELECT current_database()", String.class);
            String currentUser = jdbcTemplate.queryForObject("SELECT current_user", String.class);
            return "Koneksi DB Sukses! Terhubung ke database: " + dbName + ", sebagai user: " + currentUser;
        } catch (Exception e) {
            return "Koneksi DB Gagal: " + e.getMessage();
        }
    }

    @GetMapping("/hello")
    public  String hello() {
        return "Hello, Pey!";
    }

    @PostMapping("/analyzer")
    public  Map<Character, Integer> analyze(@RequestBody Map<String, String> body) {
        String inputKalimat = body.get("kalimat");
        CharCounter counter = new CharCounter(inputKalimat);
        Map<Character, Integer> hasil = new TreeMap<>();
        for (char c = 'a'; c <= 'z'; c++) {
            int jumlah = counter.getJumlah(c);
            if (jumlah > 0) {
                hasil.put(c, jumlah);
            }
        }
        return hasil;
    }

    @GetMapping("/find")
    public BaseResponse<List<CharAnalysisResponse>> cari(@RequestParam(required = false) String kalimat) {
        return BaseResponse.ok(charAnalysisService.cari(kalimat));
    }

    @PostMapping
    public BaseResponse<CharAnalysisResponse> simpan(
            @RequestHeader(value = "X-Request-Id", required = false) String requestId,
            @Valid @RequestBody CharAnalysisRequest request) {
        System.out.println("X-Request-Id: " + requestId + ", Kalimat: " + request.getKalimat());
        return BaseResponse.ok(charAnalysisService.simpan(request));
    }

    @GetMapping("/{id}")
    public BaseResponse<CharAnalysisResponse> detail(@PathVariable Long id) {
        CharAnalysisResponse data = charAnalysisService.findById(id);
//        if (data == null) {
//            return BaseResponse.error("Data with id : " + id + " not found");
//        }
        return BaseResponse.ok(data);
    }

    @GetMapping("/minimal")
    public BaseResponse<List<CharAnalysisResponse>> minimal(@RequestParam int min) {
        return BaseResponse.ok(charAnalysisService.findWithMinimal(min));
    }

    @GetMapping("/terbaru")
    public BaseResponse<List<CharAnalysisResponse>> terbaru() {
        return BaseResponse.ok(charAnalysisService.findNew());
    }

    @PostMapping("/from-quote")
    public BaseResponse<CharAnalysisResponse> quote() {
        return BaseResponse.ok(charAnalysisService.analyzeFromQuote());
    }

    @PostMapping("/from-quote/{id}")
    public BaseResponse<CharAnalysisResponse> quoteById(@PathVariable Long id) {
        return BaseResponse.ok(charAnalysisService.analyzeFromQuoteById(id));
    }
}
