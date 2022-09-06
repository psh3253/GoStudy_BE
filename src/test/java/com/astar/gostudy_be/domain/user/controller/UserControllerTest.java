package com.astar.gostudy_be.domain.user.controller;

import com.astar.gostudy_be.GoStudyBeApplication;
import com.astar.gostudy_be.domain.user.dto.AccountAdapter;
import com.astar.gostudy_be.domain.user.dto.ProfileDto;
import com.astar.gostudy_be.domain.user.dto.ProfileUpdateDto;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.astar.gostudy_be.domain.user.service.UserService;
import com.astar.gostudy_be.security.dto.Token;
import com.astar.gostudy_be.security.service.TokenService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    MockMvc mockMvc;

    Account account;

    @MockBean
    UserService userService;

    @MockBean
    TokenService tokenService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        account = Account.builder()
                .id(1L)
                .email("email1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("refreshToken1")
                .roles(Collections.singletonList("USER"))
                .build();
    }

    @WithMockUser
    @Test
    @DisplayName("회원가입")
    void join() throws Exception {
        // given
        Long id = 1L;
        String email = "이메일 1";
        String password = "비밀번호 1";
        String nickname = "닉네임 1";
        class JoinDto implements Serializable {
            final String email;

            final String password;

            final String nickname;

            public String getEmail() {
                return email;
            }

            public String getPassword() {
                return password;
            }

            public String getNickname() {
                return nickname;
            }

            public JoinDto(String email, String password, String nickanem) {
                this.email = email;
                this.password = password;
                this.nickname = nickanem;
            }
        }
        JoinDto joinDto = new JoinDto(email, password, nickname);

        given(userService.join(eq(email), eq(password), eq(nickname), any())).willReturn(id);

        // when & then
        mockMvc.perform(post("/api/v1/join")
                .with(csrf())
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(joinDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }

    @WithMockUser
    @Test
    @DisplayName("로그인")
    void login() throws Exception{
        // given
        String email = "email1";
        String password = "비밀번호 1";
        String accessToken = "accessToken1";
        String refreshToken = "refreshToken1";
        String role = "USER";

        class LoginDto implements Serializable {
            String email;

            String password;

            public String getEmail() {
                return email;
            }

            public String getPassword() {
                return password;
            }

            public LoginDto(String email, String password) {
                this.email = email;
                this.password = password;
            }
        }
        LoginDto loginDto = new LoginDto(email, password);

        Token token = new Token(accessToken, refreshToken);

        given(userService.login(eq(email), eq(password), any())).willReturn(account);
        given(tokenService.generateToken(eq(email), eq(role))).willReturn(token);

        // when & then
        mockMvc.perform(post("/api/v1/login")
                .with(csrf())
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(cookie().value("Auth", accessToken))
                .andExpect(cookie().value("Refresh", refreshToken))
                .andExpect(cookie().value("IsLogin", String.valueOf(true)))
                .andExpect(cookie().value("UserEmail", email))
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken));
    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("비밀번호 변경")
    void changePassword() throws Exception {
        // given
        Long id = 1L;
        String currentPassword = "비밀번호 1";
        String newPassword = "새 비밀번호 1";

        class ChangePasswordDto implements Serializable
        {
            @JsonProperty("current_password")
            String currentPassword;

            @JsonProperty("new_password")
            String newPassword;

            public String getCurrentPassword() {
                return currentPassword;
            }

            public String getNewPassword() {
                return newPassword;
            }

            public ChangePasswordDto(String currentPassword, String newPassword) {
                this.currentPassword = currentPassword;
                this.newPassword = newPassword;
            }
        }
        ChangePasswordDto changePasswordDto = new ChangePasswordDto(currentPassword, newPassword);

        given(userService.changePassword(eq(currentPassword), eq(newPassword), eq(account), any())).willReturn(id);

        // when & then
        mockMvc.perform(post("/api/v1/change-password")
                .with(csrf())
                .with(user(new AccountAdapter(account)))
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(changePasswordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("내 프로필 조회")
    void myProfile() throws Exception {
        // given
        Long id = 1L;
        String email = "이메일 1";
        String nickname = "닉네임 1";
        String image = "이미지 1";
        String introduce = "소개 1";

        ProfileDto profileDto = new ProfileDto(id, nickname, email, image, introduce);

        given(userService.getProfileByAccount(account)).willReturn(profileDto);

        // when & then
        mockMvc.perform(get("/api/v1/my-profile")
                .with(user(new AccountAdapter(account))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.image").value(image))
                .andExpect(jsonPath("$.introduce").value(introduce));
    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("프로필 수정")
    void updateProfile() throws Exception {
        // given
        Long id = 1L;
        ProfileUpdateDto profileUpdateDto = new ProfileUpdateDto("닉네임 2", true, null, "소개 2");

        given(userService.updateProfile(any(), eq(account))).willReturn(id);

        // when & then
        mockMvc.perform(patch("/api/v1/profile")
                        .with(csrf())
                        .with(user(new AccountAdapter(account)))
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(profileUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("프로필 이미지 조회")
    void showProfileImage() throws Exception {
        // given
        String filename = "default.png";

        GoStudyBeApplication.createUploadsDirectory();
        GoStudyBeApplication.copyDefaultImage();

        // when & then
        mockMvc.perform(get("/images/profile/" + filename)
                        .with(user(new AccountAdapter(account))))
                .andExpect(status().isOk());
    }
}