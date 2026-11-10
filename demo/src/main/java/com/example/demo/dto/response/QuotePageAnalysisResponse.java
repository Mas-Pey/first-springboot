package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuotePageAnalysisResponse {
    private int total;
    private int skip;
    private int limit;
    private List<CharAnalysisResponse> hasil;
}
