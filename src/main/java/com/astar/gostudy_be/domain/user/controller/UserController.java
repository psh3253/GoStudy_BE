package com.astar.gostudy_be.domain.user.controller;

import com.astar.gostudy_be.annotation.CurrentUser;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.astar.gostudy_be.domain.user.repository.AccountRepository;
import com.astar.gostudy_be.security.dto.Token;
import com.astar.gostudy_be.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AccountRepository accountRepository;

    @PostMapping("/api/v1/join")
    public Long join(@RequestBody Map<String, String> user) {
        return accountRepository.save(Account.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .nickname(user.get("nickname"))
                .roles(Collections.singletonList("ROLE_USER"))
                .build()).getId();
    }

    @PostMapping("/api/v1/login")
    public ResponseEntity<Token> login(@RequestBody Map<String, String> user, HttpServletResponse response) {
        Account member = accountRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        Token token = tokenService.generateToken(member.getEmail(), member.getRoles().get(0));
        tokenService.saveRefreshToken(token.getRefreshToken(), user.get("email"));

        ResponseCookie accessTokenCookie = ResponseCookie.from("Auth", token.getAccessToken())
                .path("/").httpOnly(true).domain("localhost").maxAge(60L * 60L * 6L).build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("Refresh", token.getRefreshToken())
                .path("/").httpOnly(true).domain("localhost").maxAge(60L * 60L * 24L * 30L).build();
        ResponseCookie isLoginCookie = ResponseCookie.from("IsLogin", "true")
                .path("/").domain("localhost").maxAge(60L * 60L * 6L).build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), refreshTokenCookie.toString(), isLoginCookie.toString())
                .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
                .body(token);
    }

    @GetMapping("/api/v1/logout")
    public ResponseEntity<Void> logout(@CurrentUser Account account, HttpServletResponse response) {
        tokenService.deleteRefreshToken(account.getEmail());

        Cookie accessTokenCookie = new Cookie("Auth", "deleted");
        accessTokenCookie.setMaxAge(-1);
        Cookie refreshTokenCookie = new Cookie("Refresh", "deleted");
        refreshTokenCookie.setMaxAge(-1);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(null);
    }
}
