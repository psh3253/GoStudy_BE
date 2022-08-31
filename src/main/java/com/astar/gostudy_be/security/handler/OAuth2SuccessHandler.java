package com.astar.gostudy_be.security.handler;

import com.astar.gostudy_be.config.Config;
import com.astar.gostudy_be.domain.user.dto.AccountDto;
import com.astar.gostudy_be.security.UserRequestMapper;
import com.astar.gostudy_be.security.dto.Token;
import com.astar.gostudy_be.security.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        AccountDto accountDto = userRequestMapper.toDto(oAuth2User);

        Token token = tokenService.generateToken(accountDto.getEmail(), "USER");

        tokenService.saveRefreshToken(token.getRefreshToken(), accountDto.getEmail());

        writeTokenResponse(response, token);
    }

    private void writeTokenResponse(HttpServletResponse response, Token token)
            throws IOException {
        Cookie accessTokenCookie = new Cookie("Auth", token.getAccessToken());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setDomain(Config.WEB_COOKIE_DOMAIN);
        accessTokenCookie.setMaxAge(60 * 60 * 6);
        Cookie refreshTokenCookie = new Cookie("Refresh", token.getRefreshToken());
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setDomain(Config.WEB_COOKIE_DOMAIN);
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30);
        Cookie isLoginCookie = new Cookie("IsLogin", "true");
        isLoginCookie.setPath("/");
        isLoginCookie.setDomain(Config.WEB_COOKIE_DOMAIN);
        isLoginCookie.setMaxAge(60 * 60 * 6);
        Cookie userEmailCookie = new Cookie("UserEmail", tokenService.getUid(token.getAccessToken()));
        userEmailCookie.setPath("/");
        userEmailCookie.setDomain(Config.WEB_COOKIE_DOMAIN);
        userEmailCookie.setMaxAge(60 * 60 * 6);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        response.addCookie(isLoginCookie);
        response.addCookie(userEmailCookie);
        response.sendRedirect(Config.WEB_URL);
    }
}
