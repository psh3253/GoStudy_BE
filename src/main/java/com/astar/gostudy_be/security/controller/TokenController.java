package com.astar.gostudy_be.security.controller;

import com.astar.gostudy_be.security.dto.Token;
import com.astar.gostudy_be.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/token/expired")
    public String auth() {
        throw new RuntimeException();
    }

    @GetMapping("/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Refresh");
        if(token != null && tokenService.verifyToken(token)) {
            String email = tokenService.getUid(token);
            if(tokenService.verifyTokenOwner(token, email)) {
                Token newToken = tokenService.generateToken(email, "USER");

                response.addHeader("Auth", newToken.getAccessToken());
                response.addHeader("Refresh", newToken.getRefreshToken());
                response.setContentType("application/json;charset=UTF-8");

                return "token";
            }
        }
        return "redirect:/";
        // 로그인 페이지로 리다이렉트 필요
    }
}
