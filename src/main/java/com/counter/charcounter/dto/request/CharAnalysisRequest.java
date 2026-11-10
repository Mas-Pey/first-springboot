package com.counter.charcounter.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CharAnalysisRequest {
    @NotBlank(message = "Kalimat wajib diisi")
    @Size(min = 1, max = 500, message = "Kalimat wajib diisi dan max 500 karakter")
    private String kalimat;
}
