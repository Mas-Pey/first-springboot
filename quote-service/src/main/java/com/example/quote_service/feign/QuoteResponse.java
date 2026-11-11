package com.example.quote_service.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteResponse {
    private Long id;
    private String quote;
    private String author;
}
