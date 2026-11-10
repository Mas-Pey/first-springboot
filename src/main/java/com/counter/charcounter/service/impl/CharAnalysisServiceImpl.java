package com.counter.charcounter.service.impl;

import com.counter.charcounter.dto.request.CharAnalysisRequest;
import com.counter.charcounter.dto.response.CharAnalysisResponse;
import com.counter.charcounter.entity.CharAnalysis;
import com.counter.charcounter.repository.CharAnalysisRepository;
import com.counter.charcounter.service.CharAnalysisService;
import com.counter.charcounter.util.CharCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CharAnalysisServiceImpl implements CharAnalysisService {

    private final CharAnalysisRepository charAnalysisRepository;
    private CharAnalysisResponse toResponse (CharAnalysis data) {
        CharCounter counter = new CharCounter(data.getKalimat());
        return CharAnalysisResponse.from(data, counter.toMap());
    }

    @Override
    public List<CharAnalysisResponse> cari(String kata) {
        List<CharAnalysis> semua;
        if (kata == null || kata.isBlank()) {
            semua = charAnalysisRepository.findAll();
        } else {
            semua = charAnalysisRepository.findByKalimatContainingIgnoreCase(kata);
        }

        List<CharAnalysisResponse> hasil = new ArrayList<>();
        for (CharAnalysis data: semua){
            hasil.add(toResponse(data));
        }
        return hasil;
    }

    @Override
    public CharAnalysisResponse simpan(CharAnalysisRequest request) {
        CharCounter counter = new CharCounter(request.getKalimat());

        CharAnalysis data = new CharAnalysis();
        data.setKalimat(request.getKalimat());
        data.setTotalHuruf(counter.getTotalHuruf());
        data.setCreatedAt(LocalDateTime.now());

        CharAnalysis saved = charAnalysisRepository.save(data);

        return CharAnalysisResponse.from(saved, counter.toMap());
    }

    @Override
    public CharAnalysisResponse findById(Long id) {
        Optional<CharAnalysis> data = charAnalysisRepository.findById(id);
        if (data.isEmpty()) {
            return null;
        }
        return toResponse(data.get());
    }

    @Override
    public List<CharAnalysisResponse> findWithMinimal(int min) {
        List<CharAnalysis> data = charAnalysisRepository.findbyMinimalHuruf(min);
        List<CharAnalysisResponse> result = new ArrayList<>();
        for (CharAnalysis d : data) {
            result.add(toResponse(d));
        }
        return result;
    }

    @Override
    public List<CharAnalysisResponse> findNew() {
        List<CharAnalysis> data = charAnalysisRepository.findNew();
        List<CharAnalysisResponse> result = new ArrayList<>();
        for (CharAnalysis d : data) {
            result.add(toResponse(d));
        }
        return result;
    }
}
