package com.astar.gostudy_be.domain.user.controller;

import com.astar.gostudy_be.annotation.CurrentUser;
import com.astar.gostudy_be.config.Config;
import com.astar.gostudy_be.domain.user.dto.ProfileDto;
import com.astar.gostudy_be.domain.user.dto.ProfileUpdateDto;
import com.astar.gostudy_be.domain.user.entity.Account;
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

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final TokenService tokenService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/v1/join")
    public Long join(@RequestBody Map<String, String> user) {
        return userService.join(user.get("email"), user.get("password"), user.get("nickname"), passwordEncoder);
    }

    @PostMapping("/api/v1/login")
    public ResponseEntity<Token> login(@RequestBody Map<String, String> user) {
        Account member = userService.login(user.get("email"), user.get("password"), passwordEncoder);

        Token token = tokenService.generateToken(member.getEmail(), member.getRoles().get(0));
        tokenService.saveRefreshToken(token.getRefreshToken(), user.get("email"));

        ResponseCookie accessTokenCookie = ResponseCookie.from("Auth", token.getAccessToken())
                .path("/").httpOnly(true).domain(Config.WEB_COOKIE_DOMAIN).maxAge(60L * 1L).build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("Refresh", token.getRefreshToken())
                .path("/").httpOnly(true).domain(Config.WEB_COOKIE_DOMAIN).maxAge(60L * 60L * 24L * 30L).build();
        ResponseCookie isLoginCookie = ResponseCookie.from("IsLogin", "true")
                .path("/").domain(Config.WEB_COOKIE_DOMAIN).maxAge(60L * 60L * 6L).build();
        ResponseCookie userEmailCookie = ResponseCookie.from("UserEmail", user.get("email"))
                .path("/").domain(Config.WEB_COOKIE_DOMAIN).maxAge(60L * 60L * 6L).build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), refreshTokenCookie.toString(), isLoginCookie.toString(), userEmailCookie.toString())
                .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
                .body(token);
    }

    @PostMapping("/api/v1/change-password")
    public Long changePassword(@RequestBody Map<String, String> user, @CurrentUser Account account) {
        return userService.changePassword(user.get("current_password"), user.get("new_password"), account, passwordEncoder);
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
    public Resource showProfileImage(@PathVariable String filename) throws MalformedURLException {
        File imageFile = new File(Config.UPLOAD_FILE_PATH + "uploads/profile/thumbnail_images/thumbnail_" + filename);
        return new UrlResource("file:" + imageFile.getAbsolutePath());
    }
}
