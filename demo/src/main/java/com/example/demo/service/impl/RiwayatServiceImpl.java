package com.example.demo.service.impl;

import com.example.demo.service.RiwayatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RiwayatServiceImpl implements RiwayatService {

    @Async
    @Override
    public void catatRiwayat(Long id, String kalimat) {
        log.info("[{}] mulai catat riwayat id={}", Thread.currentThread().getName(), id);
        try {
            Thread.sleep(3000); // pura-pura proses lama, misalnya kirim email laporan
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[{}] selesai catat riwayat id={}", Thread.currentThread().getName(), id);
    }
}
