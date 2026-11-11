package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CharAnalysisRequest {

    @NotBlank(message = "Kalimat wajib isi")
    @Size(max = 500, message = "Kalimat wajib isi sampai 500 character")
    private String kalimat;
}
