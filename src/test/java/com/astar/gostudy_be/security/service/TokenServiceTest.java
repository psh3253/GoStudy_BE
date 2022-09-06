package com.astar.gostudy_be.security.service;

import com.astar.gostudy_be.domain.user.entity.Account;
import com.astar.gostudy_be.domain.user.repository.AccountRepository;
import com.astar.gostudy_be.security.dto.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    TokenService tokenService;

    @Mock
    AccountRepository accountRepository;

    @Test
    @DisplayName("JWT 토큰 생성")
    void generateToken() {
        // given
        String secretKey = "psh3253";
        String email = "이메일 1";
        String role = "USER";

        // when
        Token token = tokenService.generateToken(email, role);

        // then
        assertThat(Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token.getAccessToken()).getBody().getSubject()).isEqualTo(email);
        assertThat(Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token.getRefreshToken()).getBody().getSubject()).isEqualTo(email);
    }

    @Test
    @DisplayName("JWT 토큰 검증")
    void verifyToken() {
        // given
        String email = "이메일 1";
        String role = "USER";
        String secretKey = "psh3253";
        Boolean expectedResult = true;

        long accessTokenPeriod = 1000L * 60L * 60L * 6L;
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // when
        Boolean result = tokenService.verifyToken(token);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("토큰 소유자 검증")
    void verifyTokenOwner() {
        // given
        String email = "이메일 1";
        String role = "USER";
        String secretKey = "psh3253";
        Boolean expectedResult = true;

        long refreshTokenPeriod = 1000L * 60L * 60L * 24L * 30L;
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        Account account = Account.builder()
                .id(1L)
                .email(email)
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken(token)
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.ofNullable(account));

        // when
        Boolean result = tokenService.verifyTokenOwner(token, email);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("토큰으로부터 사용자 이메일 조회")
    void getUid() {
        // given
        String email = "이메일 1";
        String role = "USER";
        String secretKey = "psh3253";

        long accessTokenPeriod = 1000L * 60L * 60L * 6L;
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // when
        String uid = tokenService.getUid(token);

        // then
        assertThat(uid).isEqualTo(email);
    }

    @Test
    @DisplayName("Refresh 토큰 저장")
    void saveRefreshToken() {
        // given
        Long id = 1L;
        String email = "이메일 1";
        String role = "USER";
        String secretKey = "psh3253";

        long refreshTokenPeriod = 1000L * 60L * 60L * 24L * 30L;
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        Account account = Account.builder()
                .id(id)
                .email(email)
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.ofNullable(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // when
        Long accountId = tokenService.saveRefreshToken(token, email);

        // then
        assertThat(accountId).isEqualTo(id);
    }

    @Test
    @DisplayName("Refresh 토큰 삭제")
    void deleteRefreshToken() {
        // given
        Long id = 1L;
        String email = "이메일 1";

        Account account = Account.builder()
                .id(id)
                .email(email)
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.ofNullable(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // when
        Long accountId = tokenService.deleteRefreshToken(email);

        // then
        assertThat(accountId).isEqualTo(id);
    }
}