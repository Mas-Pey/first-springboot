package com.counter.charcounter.repository;

import com.counter.charcounter.entity.CharAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharAnalysisRepository extends JpaRepository<CharAnalysis, Long> {
    List<CharAnalysis> findByKalimatContainingIgnoreCase(String kata);
}
