package com.astar.gostudy_be.security.filter;

import com.astar.gostudy_be.domain.user.entity.Account;
import com.astar.gostudy_be.domain.user.dto.AccountAdapter;
import com.astar.gostudy_be.domain.user.service.UserService;
import com.astar.gostudy_be.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {
    private final TokenService tokenService;

    private final UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader("Auth");
        String refreshToken = ((HttpServletRequest) request).getHeader("Refresh");

        if (token != null) {
            if(tokenService.verifyToken(token)) {
                String email = tokenService.getUid(token);

                Account account = userService.getAccount(email);

                Authentication auth = getAuthentication(account);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            else {
                ((HttpServletResponse)response).sendRedirect("/token/refresh");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(Account account) {
        return new UsernamePasswordAuthenticationToken(new AccountAdapter(account), "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
