package com.astar.gostudy_be.security.filter;

import com.astar.gostudy_be.domain.user.entity.Account;
import com.astar.gostudy_be.domain.user.dto.AccountAdapter;
import com.astar.gostudy_be.domain.user.service.UserService;
import com.astar.gostudy_be.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {
    private final TokenService tokenService;

    private final UserService userService;

    private final HashSet<String> allowedPostURI = new HashSet<>(Arrays.asList("/api/v1/join", "/api/v1/login"));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Cookie[] cookies = httpRequest.getCookies();
        String token = null;
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Auth"))
                    token = cookie.getValue();
                else if (cookie.getName().equals("Refresh"))
                    refreshToken = cookie.getValue();
            }
        }
        if(Objects.equals(httpRequest.getMethod(), "OPTIONS"))
        {
            chain.doFilter(request, response);
            return;
        }
        // 로그인 또는 회원가입
        if(allowedPostURI.contains(httpRequest.getRequestURI()) && Objects.equals(httpRequest.getMethod(), "POST"))
        {
            chain.doFilter(request, response);
            return;
        }
        // 토큰 재발급 링크인 경우
        if(Objects.equals(httpRequest.getRequestURI(), "/api/v1/token/refresh"))
        {
            chain.doFilter(request, response);
            return;
        }
        // 메인 페이지 또는 이미지인 경우
        if((Objects.equals(httpRequest.getRequestURI(), "/api/v1/studies") || httpRequest.getRequestURI().startsWith("/images")) && Objects.equals(httpRequest.getMethod(), "GET")) {
            chain.doFilter(request, response);
            return;
        }

        // token이 존재하는 경우
        if(token != null && !Objects.equals(token, "deleted"))
        {
            if (!tokenService.verifyToken(token)) { // 토큰 유효기간 만료
                ((HttpServletResponse) response).sendRedirect("/api/v1/token/refresh");
                return;
            }
            // 인증 성공
            String email = tokenService.getUid(token);

            Account account = userService.getAccount(email);

            Authentication auth = getAuthentication(account);
            SecurityContextHolder.getContext().setAuthentication(auth);

            chain.doFilter(request, response);
            return;
        }
        // 토큰 미존재 && 리프레쉬 토큰 존재
        if(token == null && refreshToken != null)
        {
            ((HttpServletResponse) response).sendRedirect("/api/v1/token/refresh");
            return;
        }
        // 리프레쉬 토큰 미존재
        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(Account account) {
        return new UsernamePasswordAuthenticationToken(new AccountAdapter(account), "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
