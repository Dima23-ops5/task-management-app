package com.example.task_management_app.security;

import com.example.task_management_app.dto.UserLoginRequestDto;
import com.example.task_management_app.dto.UserLoginResponseDto;
import com.example.task_management_app.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        requestDto.email(),
                        requestDto.password()));
        return new UserLoginResponseDto(jwtUtil.generateToken(
                authentication.getName()));
    }
}
