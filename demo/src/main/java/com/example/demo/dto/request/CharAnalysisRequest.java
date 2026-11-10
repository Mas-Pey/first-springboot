package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CharAnalysisRequest {

    @NotBlank(message = "Kalimat wajib isi")
    @Size(max = 255, message = "Kalimat wajib isi sampai 255 character")
    private String kalimat;
}
