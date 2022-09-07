package com.astar.gostudy_be.security.controller;

import com.astar.gostudy_be.config.Config;
import com.astar.gostudy_be.security.dto.Token;
import com.astar.gostudy_be.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/api/v1/token/refresh")
    public ResponseEntity<Token> refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        String token = null;
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Refresh"))
                    refreshToken = cookie.getValue();
            }
        }
        if(refreshToken != null && tokenService.verifyToken(refreshToken)) {
            String email = tokenService.getUid(refreshToken);
            if(tokenService.verifyTokenOwner(refreshToken, email)) {
                Token newToken = tokenService.generateToken(email, "USER");
                tokenService.saveRefreshToken(newToken.getRefreshToken(), email);

                ResponseCookie accessTokenCookie = ResponseCookie.from("Auth", newToken.getAccessToken())
                        .path("/").httpOnly(true).domain(Config.WEB_COOKIE_DOMAIN).maxAge(60L * 1L).build();
                ResponseCookie refreshTokenCookie = ResponseCookie.from("Refresh", newToken.getRefreshToken())
                        .path("/").httpOnly(true).domain(Config.WEB_COOKIE_DOMAIN).maxAge(60L * 60L * 24L * 30L).build();
                ResponseCookie isLoginCookie = ResponseCookie.from("IsLogin", "true")
                        .path("/").domain(Config.WEB_COOKIE_DOMAIN).maxAge(60L * 60L * 6L).build();
                ResponseCookie userEmailCookie = ResponseCookie.from("UserEmail", email)
                        .path("/").domain(Config.WEB_COOKIE_DOMAIN).maxAge(60L * 60L * 6L).build();

                return ResponseEntity.status(HttpStatus.OK)
                        .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), refreshTokenCookie.toString(), isLoginCookie.toString(), userEmailCookie.toString())
                        .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
                        .body(newToken);
            }
        }
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(Config.WEB_URL + "/login"))
                .build();
    }
}
