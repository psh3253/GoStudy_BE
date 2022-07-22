package com.astar.gostudy_be.security;

import com.astar.gostudy_be.domain.user.dto.AccountDto;
import com.astar.gostudy_be.security.dto.Token;
import com.astar.gostudy_be.security.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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

        response.addHeader("Auth", token.getAccessToken());
        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}
