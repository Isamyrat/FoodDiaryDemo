package com.food.diary.service;

import com.food.diary.DTO.LoginDTO;

public interface AuthService {
    String authenticateAndGetToken(final LoginDTO loginDTO);
}
