package com.counter.charcounter.service;

import com.counter.charcounter.dto.response.CharAnalysisResponse;
import com.counter.charcounter.entity.CharAnalysis;

import java.util.List;

public interface CharAnalysisService {
    List<CharAnalysisResponse> cari(String kata);
}
