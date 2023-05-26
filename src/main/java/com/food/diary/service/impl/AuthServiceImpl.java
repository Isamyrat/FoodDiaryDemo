package com.food.diary.service.impl;

import com.food.diary.DTO.LoginDTO;
import com.food.diary.security.TokenProvider;
import com.food.diary.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Override
    public String authenticateAndGetToken(LoginDTO loginDTO) {
        Authentication
            authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        return tokenProvider.generate(authentication);
    }
}
