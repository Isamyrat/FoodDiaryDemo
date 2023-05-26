package com.food.diary.controller;

import com.food.diary.DTO.LoginDTO;
import com.food.diary.response.BaseResponse;
import com.food.diary.response.user.AuthResponse;
import com.food.diary.service.AuthService;
import com.food.diary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/authenticate")
    public AuthResponse login(@RequestBody LoginDTO loginDTO) {
        String token = authService.authenticateAndGetToken(loginDTO);
        return new AuthResponse(token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create-curator")
    public ResponseEntity<BaseResponse> createCurator(@Valid @RequestBody LoginDTO loginDTO) {
        BaseResponse baseResponse;
        if (userService.hasUserWithUsername(loginDTO.getUsername())) {
            baseResponse =
                new BaseResponse(false, "Username " + loginDTO.getUsername() + " already been used", "Error");
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }

        userService.createCurator(loginDTO);
        baseResponse = new BaseResponse(true, "", "");

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create-user")
    public ResponseEntity<BaseResponse> createUser(@Valid @RequestBody LoginDTO loginDTO) {
        BaseResponse baseResponse;
        if (userService.hasUserWithUsername(loginDTO.getUsername())) {
            baseResponse =
                new BaseResponse(false, "Username " + loginDTO.getUsername() + " already been used", "Error");
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }

        userService.createUser(loginDTO);

        baseResponse = new BaseResponse(true, "", "");
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
