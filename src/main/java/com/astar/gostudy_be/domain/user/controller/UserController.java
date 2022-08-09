package com.astar.gostudy_be.domain.user.controller;

import com.astar.gostudy_be.annotation.CurrentUser;
import com.astar.gostudy_be.domain.user.dto.ProfileDto;
import com.astar.gostudy_be.domain.user.dto.ProfileUpdateDto;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.astar.gostudy_be.domain.user.repository.AccountRepository;
import com.astar.gostudy_be.domain.user.service.UserService;
import com.astar.gostudy_be.security.dto.Token;
import com.astar.gostudy_be.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AccountRepository accountRepository;

    private final UserService userService;

    @PostMapping("/api/v1/join")
    public Long join(@RequestBody Map<String, String> user) {
        return accountRepository.save(Account.builder()
                .email(user.get("email"))
                .image("thumbnail_default.png")
                .password(passwordEncoder.encode(user.get("password")))
                .nickname(user.get("nickname"))
                .introduce("")
                .roles(Collections.singletonList("ROLE_USER"))
                .build()).getId();
    }

    @PostMapping("/api/v1/login")
    public ResponseEntity<Token> login(@RequestBody Map<String, String> user, HttpServletResponse response) {
        Account member = accountRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        Token token = tokenService.generateToken(member.getEmail(), member.getRoles().get(0));
        tokenService.saveRefreshToken(token.getRefreshToken(), user.get("email"));

        ResponseCookie accessTokenCookie = ResponseCookie.from("Auth", token.getAccessToken())
                .path("/").httpOnly(true).domain("localhost").maxAge(60L * 60L * 6L).build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("Refresh", token.getRefreshToken())
                .path("/").httpOnly(true).domain("localhost").maxAge(60L * 60L * 24L * 30L).build();
        ResponseCookie isLoginCookie = ResponseCookie.from("IsLogin", "true")
                .path("/").domain("localhost").maxAge(60L * 60L * 6L).build();
        ResponseCookie userEmailCookie = ResponseCookie.from("UserEmail", user.get("email"))
                .path("/").domain("localhost").maxAge(60L * 60L * 6L).build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), refreshTokenCookie.toString(), isLoginCookie.toString(), userEmailCookie.toString())
                .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
                .body(token);
    }

    @PostMapping("/api/v1/change-password")
    public Long changePassword(@RequestBody Map<String, String> user, @CurrentUser Account account) {
        if (!passwordEncoder.matches(user.get("current_password"), account.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return accountRepository.save(account.update(null, passwordEncoder.encode(user.get("new_password")), null, null, null, null)).getId();
    }

    @GetMapping("/api/v1/my-profile")
    public ProfileDto myProfile(@CurrentUser Account account) {
        return userService.getProfileByAccount(account);
    }

    @PatchMapping("/api/v1/profile")
    public Long updateProfile(@ModelAttribute ProfileUpdateDto profileUpdateDto, @CurrentUser Account account) {
        return userService.updateProfile(profileUpdateDto, account);
    }

    @ResponseBody
    @GetMapping("/images/profile/{filename}")
    public Resource showStudyImage(@PathVariable String filename) throws MalformedURLException {
        File imageFile = new File("C://uploads/profile/thumbnail_images/thumbnail_" + filename);
        return new UrlResource("file:" + imageFile.getAbsolutePath());
    }
}
