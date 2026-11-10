package com.example.demo.service.impl;

import com.example.demo.dto.request.CharAnalysisRequest;
import com.example.demo.dto.response.CharAnalysisResponse;
import com.example.demo.entity.CharAnalysis;
import com.example.demo.exception.AnalysisAlreadyExistException;
import com.example.demo.exception.AnalysisNotFoundException;
import com.example.demo.repository.CharAnalysisRepository;
import com.example.demo.service.CharAnalysisService;
import com.example.demo.util.CharCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__( @Autowired))
public class CharAnalysisServiceImpl implements CharAnalysisService {

    private final CharAnalysisRepository charAnalysisRepository;

    @Override
    public List<CharAnalysisResponse> cari(String kata) {
        List<CharAnalysis> semua;
        if (kata == null || kata.isBlank()) {
            semua = charAnalysisRepository.findAll();
        } else {
            semua = charAnalysisRepository.findByKalimatContainingIgnoreCase(kata);
        }

        List<CharAnalysisResponse> hasil = new ArrayList<>();
        for (CharAnalysis data: semua) {
            hasil.add(toResponse(data));
        }
        return hasil;
    }

    @Override
    public CharAnalysisResponse analisis(CharAnalysisRequest request) {
        String kalimat = request.getKalimat().trim();
        if (charAnalysisRepository.existsByKalimatIgnoreCase(kalimat)) {
            throw new AnalysisAlreadyExistException(kalimat);
        }

        CharCounter counter = new CharCounter(kalimat);
        CharAnalysis data = new CharAnalysis();

        data.setKalimat(kalimat);
        data.setTotalHuruf(counter.getTotalHuruf());
        data.setCreatedAt(LocalDateTime.now());
        charAnalysisRepository.save(data);

        return CharAnalysisResponse.from(data, counter.toMap());
    }

    @Override
    public CharAnalysisResponse cariById(Long id) {
        CharAnalysis data = charAnalysisRepository.findById(id)
                .orElseThrow(() -> new AnalysisNotFoundException(id));
        return toResponse(data);
    }

    @Override
    public List<CharAnalysisResponse> cariMinimalHuruf(int min) {
        return toResponseList(charAnalysisRepository.cariMinimalHuruf(min));
    }

    @Override
    public List<CharAnalysisResponse> limaTerbaru() {
        return toResponseList(charAnalysisRepository.limaTerbaru());
    }

    private List<CharAnalysisResponse> toResponseList(List<CharAnalysis> semua) {
        List<CharAnalysisResponse> hasil = new ArrayList<>();
        for (CharAnalysis data : semua) {
            hasil.add(toResponse(data));
        }
        return hasil;
    }

    private CharAnalysisResponse toResponse(CharAnalysis data) {
        CharCounter counter = new CharCounter(data.getKalimat());
        return CharAnalysisResponse.from(data, counter.toMap());
    }
}
