package com.example.demo.service.impl;

import com.example.demo.dto.request.AuthRequestDto;
import com.example.demo.dto.response.AuthResponseDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public String register(AuthRequestDto authRequestDto) {
        if(userRepository.existsByUsername(authRequestDto.getUsername())){
            throw new UserAlreadyExistException(authRequestDto.getUsername());
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(authRequestDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(authRequestDto.getPassword()));
        userRepository.save(userEntity);

        return "User " + authRequestDto.getUsername() + " has been registered successfully";
    }

    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        UserEntity userEntity = userRepository.findByUsername(authRequestDto.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Username/password salah"));

        if(!passwordEncoder.matches(authRequestDto.getPassword(), userEntity.getPassword())){
            throw new UnauthorizedException("Username/password salah");
        }

        String token = jwtUtil.generateToken(userEntity.getUsername());

        return new AuthResponseDto(userEntity.getUsername(),token);
    }

}