package com.counter.charcounter.dto.response;

import com.counter.charcounter.entity.CharAnalysis;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class CharAnalysisResponse {
    private  Long id;
    private String kalimat;
    private String author;
    private int totalHuruf;
    private Map<Character, Integer> frekuensi;

    public static CharAnalysisResponse from(CharAnalysis data, Map<Character, Integer> frekuensi) {
        return new CharAnalysisResponse(data.getId(), data.getKalimat(), data.getAuthor(), data.getTotalHuruf(), frekuensi);
    }
}
