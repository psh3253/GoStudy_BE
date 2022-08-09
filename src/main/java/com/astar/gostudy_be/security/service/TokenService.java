package com.astar.gostudy_be.security.service;

import com.astar.gostudy_be.domain.user.entity.Account;
import com.astar.gostudy_be.domain.user.repository.AccountRepository;
import com.astar.gostudy_be.security.dto.Token;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TokenService {
    private String secretKey = "psh3253";

    private final AccountRepository accountRepository;

    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Token generateToken(String uid, String role) {
        long accessTokenPeriod = 1000L * 60L * 60L * 6L;
        long refreshTokenPeriod = 1000L * 60L * 60L * 24L * 30L;

        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("role", role);

        Date now = new Date();
        return new Token(
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + accessTokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact(),
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshTokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact()
        );
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyTokenOwner(String token, String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        return Objects.equals(account.getRefreshToken(), token);
    }

    public String getUid(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    @Transactional
    public void saveRefreshToken(String token, String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        account.update(null, null, null, null,  null, token);
        accountRepository.save(account);
    }

    @Transactional
    public void deleteRefreshToken(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        account.update(null, null, null, null, null, "deleted");
        accountRepository.save(account);
    }
}
