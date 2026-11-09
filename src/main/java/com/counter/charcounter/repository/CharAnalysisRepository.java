package com.counter.charcounter.repository;

import com.counter.charcounter.entity.CharAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CharAnalysisRepository extends JpaRepository<CharAnalysis, Long> {
    List<CharAnalysis> findByKalimatContainingIgnoreCase(String kata);
    @Query("select c from CharAnalysis c where c.totalHuruf >= :min") List<CharAnalysis> findbyMinimalHuruf(@Param("min") int min);
}
