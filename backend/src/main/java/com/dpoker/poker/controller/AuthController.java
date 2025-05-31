package com.dpoker.poker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dpoker.poker.dto.auth.JWTDetails;
import com.dpoker.poker.security.JWTUtil;
import com.dpoker.poker.service.UserService;
import com.dpoker.poker.model.User;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    public AuthController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<JWTDetails> register(@RequestParam String username, @RequestParam String password) {
        try {
            User user = this.userService.register(username, password);
            JWTDetails jwtDetails = JWTDetails.builder()
                .accessToken(jwtUtil.generateAccessToken(user.getUsername()))
                .refreshToken(jwtUtil.generateRefreshToken(user.getUsername()))
                .build();

                return ResponseEntity.ok(jwtDetails);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JWTDetails> login(@RequestParam String username, @RequestParam String password) {
        if (userService.authenticate(username, password)) {
            JWTDetails jwtDetails = JWTDetails.builder()
                .accessToken(jwtUtil.generateAccessToken(username))
                .refreshToken(jwtUtil.generateRefreshToken(username))
                .build();

            return ResponseEntity.ok(jwtDetails);
        } else {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid username or password"
            );
        }
    }
}
