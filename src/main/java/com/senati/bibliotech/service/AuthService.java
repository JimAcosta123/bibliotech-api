package com.senati.bibliotech.service;

import com.senati.bibliotech.dto.AuthResponse;
import com.senati.bibliotech.dto.LoginRequest;
import com.senati.bibliotech.dto.RegisterRequest;
import com.senati.bibliotech.exception.BadRequestException;
import com.senati.bibliotech.model.Role;
import com.senati.bibliotech.model.User;
import com.senati.bibliotech.repository.UserRepository;
import com.senati.bibliotech.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Ya existe un usuario con ese email");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), token);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Credenciales invalidas");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Credenciales invalidas"));

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), token);
    }
}
