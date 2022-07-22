package com.astar.gostudy_be.security.config;

import com.astar.gostudy_be.domain.user.service.UserService;
import com.astar.gostudy_be.security.filter.JwtAuthFilter;
import com.astar.gostudy_be.security.OAuth2SuccessHandler;
import com.astar.gostudy_be.security.service.CustomOAuth2Service;
import com.astar.gostudy_be.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final CustomOAuth2Service oAuth2Service;
    private final OAuth2SuccessHandler successHandler;
    private final TokenService tokenService;
    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception{
//        return super.authenticationManagerBean();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/**").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .successHandler(successHandler)
                .userInfoEndpoint().userService(oAuth2Service);
                http.addFilterBefore(new JwtAuthFilter(tokenService, userService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
