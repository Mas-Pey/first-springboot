package com.counter.charcounter.service;

import com.counter.charcounter.dto.request.CharAnalysisRequest;
import com.counter.charcounter.dto.response.CharAnalysisResponse;

import java.util.List;

public interface CharAnalysisService {
    List<CharAnalysisResponse> cari(String kata);
    CharAnalysisResponse simpan(CharAnalysisRequest request);
}
