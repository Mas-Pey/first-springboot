package com.counter.charcounter.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class CharCounter {
//    private static int jumlahObjek = 0;
    private final String kalimat;
    // Contoh penggunaan Instance:
    private final int[] freq = new int[26];

    public CharCounter(String kalimat) {
        this.kalimat = kalimat;
        for (char c : kalimat.toLowerCase().toCharArray()) {
            if (c < 'a' || c > 'z') continue;
            freq[c - 'a']++;
        }
//        jumlahObjek++;
//        hitung();
    }

//    private void hitung() {
//        for (char c : kalimat.toLowerCase().toCharArray()) {
//            if (c < 'a' || c > 'z') continue;
//            freq[c - 'a']++;
//        }
//    }

    public int getJumlah(char huruf) {
        huruf = Character.toLowerCase(huruf);
        if (huruf < 'a' || huruf > 'z') return 0;
        return  freq[huruf - 'a'];
    }

    public String getKalimat() {
        return kalimat;
    }

//    public static int getJumlahObjek() {
//        return jumlahObjek;
//    }

    //Overloading Method
//    public void tampilkan() {
//        System.out.println("cek");
//    }

//    public void tampilkan(int minimum) {
//        System.out.println(getClass().getSimpleName() + " -> \"" + kalimat + "\"");
//        for (int i = 0; i < 26; i++) {
//            if (freq[i] >= minimum) {
//                System.out.println(" " + (char) ('a' + i) + freq[i]);
//            }
//        }
//    }

    // ==========================================================
    // DAY 2
    public int getTotalHuruf() {
        int total = 0;
        for (int jumlah: freq) {
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


