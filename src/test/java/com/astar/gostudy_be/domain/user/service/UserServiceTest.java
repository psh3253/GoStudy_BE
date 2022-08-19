package com.astar.gostudy_be.domain.user.service;

import com.astar.gostudy_be.domain.user.dto.AccountAdapter;
import com.astar.gostudy_be.domain.user.dto.ProfileDto;
import com.astar.gostudy_be.domain.user.dto.ProfileUpdateDto;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.astar.gostudy_be.domain.user.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    AccountRepository accountRepository;

    @Test
    void getAccount() {
        // given
        Long id = 1L;
        String email = "이메일 1";
        String password = "비밀번호 1";
        String nickname = "닉네임 1";
        String image = "이미지 1";
        String introduce = "소개 1";
        String refreshToken = "리프레쉬 토큰 1";

        Account account = Account.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickname(nickname)
                .image(image)
                .introduce(introduce)
                .refreshToken(refreshToken)
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.ofNullable(account));

        // when
        Account userAccount = userService.getAccount(email);

        // then
        assertThat(userAccount.getId()).isEqualTo(id);
        assertThat(userAccount.getEmail()).isEqualTo(email);
        assertThat(userAccount.getPassword()).isEqualTo(password);
        assertThat(userAccount.getNickname()).isEqualTo(nickname);
        assertThat(userAccount.getImage()).isEqualTo(image);
        assertThat(userAccount.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    void loadUserByUsername() {
        // given
        Long id = 1L;
        String email = "이메일 1";
        String password = "비밀번호 1";
        String nickname = "닉네임 1";
        String image = "이미지 1";
        String introduce = "소개 1";
        String refreshToken = "리프레쉬 토큰 1";

        Account account = Account.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickname(nickname)
                .image(image)
                .introduce(introduce)
                .refreshToken(refreshToken)
                .roles(Collections.singletonList("USER"))
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.ofNullable(account));

        // when
        AccountAdapter accountAdapter = (AccountAdapter) userService.loadUserByUsername(email);

        // then
        assertThat(accountAdapter.getAccount().getId()).isEqualTo(id);
        assertThat(accountAdapter.getAccount().getEmail()).isEqualTo(email);
        assertThat(accountAdapter.getAccount().getPassword()).isEqualTo(password);
        assertThat(accountAdapter.getAccount().getNickname()).isEqualTo(nickname);
        assertThat(accountAdapter.getAccount().getImage()).isEqualTo(image);
        assertThat(accountAdapter.getAccount().getIntroduce()).isEqualTo(introduce);
        assertThat(accountAdapter.getAccount().getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    void getProfileByAccount() {
        // given
        Long id = 1L;
        String email = "이메일 1";
        String nickname = "닉네임 1";
        String image = "이미지 1";
        String introduce = "소개 1";

        Account account = Account.builder()
                .id(id)
                .email(email)
                .password("비밀번호 1")
                .nickname(nickname)
                .image(image)
                .introduce(introduce)
                .refreshToken("리프레쉬 토큰 1")
                .build();

        // when
        ProfileDto profileDto = userService.getProfileByAccount(account);

        // then
        assertThat(profileDto.getId()).isEqualTo(id);
        assertThat(profileDto.getEmail()).isEqualTo(email);
        assertThat(profileDto.getNickname()).isEqualTo(nickname);
        assertThat(profileDto.getIntroduce()).isEqualTo(introduce);
        assertThat(profileDto.getImage()).isEqualTo(image);
    }

    @Test
    void updateProfile() {
        // given
        Long id = 1L;
        ProfileUpdateDto profileUpdateDto = new ProfileUpdateDto("닉네임 2", true, null, "소개 2");
        Account account = Account.builder()
                .id(id)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(account.update(profileUpdateDto.getNickname(), null, "default.jpg", profileUpdateDto.getIntroduce(), null, null));

        // when
        Long accountId = userService.updateProfile(profileUpdateDto, account);

        // then
        assertThat(accountId).isEqualTo(id);
    }
}