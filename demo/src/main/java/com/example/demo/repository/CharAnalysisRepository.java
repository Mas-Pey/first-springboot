package com.example.demo.repository;

import com.example.demo.entity.CharAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CharAnalysisRepository extends JpaRepository<CharAnalysis, Long> {
    List<CharAnalysis> findByKalimatContainingIgnoreCase(String kata);
    @Query("SELECT c FROM CharAnalysis c WHERE c.totalHuruf >= :min")
    List<CharAnalysis> cariMinimalHuruf(@Param("min") int min);
    @Query(value = "SELECT * FROM mst_char_analysis ORDER BY created_at DESC LIMIT 5",
            nativeQuery = true)
    List<CharAnalysis> limaTerbaru();
}
