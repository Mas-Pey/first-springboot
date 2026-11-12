package com.example.demo.controller;

import com.example.demo.dto.request.AuthRequestDto;
import com.example.demo.dto.response.AuthResponseDto;
import com.example.demo.dto.response.BaseResponse;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public BaseResponse<String> register(
            @Valid
            @RequestBody
            AuthRequestDto authRequestDto) {
        return BaseResponse.ok(authService.register(authRequestDto));
    }

    @PostMapping("/login")
    public BaseResponse<AuthResponseDto> login(
            @Valid
            @RequestBody
            AuthRequestDto authRequestDto) {
        return BaseResponse.ok(authService.login(authRequestDto));
    }
}
