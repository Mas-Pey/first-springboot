package com.example.quote_service.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteListResponse {
    private List<QuoteResponse> quotes;
    private int total;
    private int skip;
    private int limit;
}
