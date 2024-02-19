package com.daniel.security_jwt.service;

import lombok.RequiredArgsConstructor;

import com.daniel.security_jwt.user.User;
import com.daniel.security_jwt.user.ERole;
import com.daniel.security_jwt.user.UserDTO;
import com.daniel.security_jwt.mapper.UserMapper;
import com.daniel.security_jwt.repository.IUserRepository;
import com.daniel.security_jwt.auth.AuthenticationRequest;
import com.daniel.security_jwt.auth.AuthenticationResponse;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passEncoder;
    private final IUserRepository userRepository;
    private final AuthenticationManager authManager;

    public AuthenticationResponse register(final UserDTO userRequest) {
        final User user = this.userMapper.mapFrom(userRequest);
        user.setPass(this.passEncoder.encode(userRequest.getPassword()));
        user.setRole(ERole.USER);
        this.userRepository.save(user);

        final String jwtToken = this.jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(final AuthenticationRequest authRequest) {
        this.authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        final User user = this.userRepository.findUserByEmail(authRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(authRequest.getEmail() +" not found!"));

        final String token = this.jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

}