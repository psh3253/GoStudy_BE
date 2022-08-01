package com.astar.gostudy_be.security.handler;

import com.astar.gostudy_be.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final TokenService tokenService;

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("Auth")).collect(Collectors.toList()).get(0).getValue();
        String email = tokenService.getUid(token);
        tokenService.deleteRefreshToken(email);

        Cookie accessTokenCookie = new Cookie("Auth", "deleted");
        accessTokenCookie.setMaxAge(-1);

        Cookie refreshTokenCookie = new Cookie("Refresh", "deleted");
        refreshTokenCookie.setMaxAge(-1);

        Cookie isLoginCookie = new Cookie("IsLogin", "false");
        isLoginCookie.setMaxAge(-1);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        response.addCookie(isLoginCookie);
        response.sendRedirect("http://localhost:8080/");
    }
}