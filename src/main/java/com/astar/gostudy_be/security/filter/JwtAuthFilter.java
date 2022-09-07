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

    private final HashSet<String> allowedURI = new HashSet<>(Arrays.asList("/api/v1/token/refresh", "/api/v1/join", "/api/v1/login", "/api/v1/studies"));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
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

        // token이 존재하는 경우
        if (!allowedURI.contains(((HttpServletRequest) request).getRequestURI()) && !((HttpServletRequest) request).getRequestURI().startsWith("/images") && token != null && !Objects.equals(token, "deleted")) {
            if (!tokenService.verifyToken(token)) { // 토큰 유효기간 만료
                ((HttpServletResponse) response).sendRedirect("/api/v1/token/refresh");
                return;
            } else { // 로그인 성공
                String email = tokenService.getUid(token);

                Account account = userService.getAccount(email);

                Authentication auth = getAuthentication(account);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        else if(!allowedURI.contains(((HttpServletRequest) request).getRequestURI()) && !((HttpServletRequest) request).getRequestURI().startsWith("/images") && token == null && refreshToken != null) {
            ((HttpServletResponse) response).sendRedirect("/api/v1/token/refresh?redirection-uri=" + ((HttpServletRequest) request).getRequestURI());
            return;
        }

        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(Account account) {
        return new UsernamePasswordAuthenticationToken(new AccountAdapter(account), "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
