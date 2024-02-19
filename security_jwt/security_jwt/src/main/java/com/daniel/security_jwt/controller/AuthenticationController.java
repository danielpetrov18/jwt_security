package com.daniel.security_jwt.controller;

import lombok.RequiredArgsConstructor;

import com.daniel.security_jwt.user.UserDTO;
import com.daniel.security_jwt.auth.AuthenticationRequest;
import com.daniel.security_jwt.auth.AuthenticationResponse;
import com.daniel.security_jwt.service.AuthenticationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody final UserDTO request) {
        return ResponseEntity.ok(this.authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody final AuthenticationRequest request) {
        return ResponseEntity.ok(this.authService.authenticate(request));
    }

}