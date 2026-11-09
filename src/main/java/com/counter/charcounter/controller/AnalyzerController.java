package com.counter.charcounter.controller;

import com.counter.charcounter.dto.response.BaseResponse;
import com.counter.charcounter.dto.response.CharAnalysisResponse;
import com.counter.charcounter.service.CharAnalysisService;
import com.counter.charcounter.util.CharCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// Rest : HTTP, berbeda dengan only @Controller: output html
@RestController
@RequestMapping("/api")
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

    //Post /find
}
