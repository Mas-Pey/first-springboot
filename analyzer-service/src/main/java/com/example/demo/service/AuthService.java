package com.example.demo.service;

import com.example.demo.dto.request.AuthRequestDto;
import com.example.demo.dto.response.AuthResponseDto;

public interface AuthService {
    String register(AuthRequestDto authRequestDto);
    AuthResponseDto login(AuthRequestDto authRequestDto);
}
