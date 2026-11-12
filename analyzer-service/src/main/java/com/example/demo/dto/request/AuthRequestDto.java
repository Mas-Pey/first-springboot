package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequestDto {
    @NotBlank(message = "username wajib diisi")
    private String username;

    @NotBlank(message = "password wajib diisi")
    @Size(min = 8, message = "password minimal 8 karakter")
    private String password;

}