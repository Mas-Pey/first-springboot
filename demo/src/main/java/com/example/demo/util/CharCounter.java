package com.example.demo.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class CharCounter {

    private final String kalimat;
    private final int[] freq = new int[26];

    public CharCounter(String kalimat) {
        this.kalimat = kalimat;
        for (char c : kalimat.toLowerCase().toCharArray()) {
            if (c < 'a' || c > 'z') continue;
            freq[c - 'a']++;
        }
    }

    public int getJumlah(char huruf) {
        if (huruf < 'a' || huruf > 'z') return 0;
        return freq[huruf - 'a'];
    }

    public int getTotalHuruf() {
        int total = 0;
        for (int jumlah : freq) {
            total += jumlah;
        }
        return total;
    }

    public Map<Character, Integer> toMap() {
        Map<Character, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                map.put((char) ('a' + i), freq[i]);
            }
        }
        return map;
    }
}
