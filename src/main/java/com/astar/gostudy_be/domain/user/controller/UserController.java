package com.astar.gostudy_be.domain.user.controller;

import com.astar.gostudy_be.domain.user.entity.Account;
import com.astar.gostudy_be.domain.user.repository.AccountRepository;
import com.astar.gostudy_be.security.dto.Token;
import com.astar.gostudy_be.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AccountRepository accountRepository;

    @PostMapping("/join")
    public Long join(@RequestBody Map<String, String> user) {
        return accountRepository.save(Account.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .nickname(user.get("nickname"))
                .roles(Collections.singletonList("ROLE_USER"))
                .build()).getId();
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody Map<String, String> user) {
        Account member = accountRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        Token token = tokenService.generateToken(member.getEmail(), member.getRoles().get(0));
        return ResponseEntity.status(HttpStatus.OK)
                .header("Auth", token.getAccessToken())
                .header("Refresh", token.getRefreshToken())
                .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
                .body(token);
    }
}
