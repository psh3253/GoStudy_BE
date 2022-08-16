package com.astar.gostudy_be.domain.user.repository;

import com.astar.gostudy_be.domain.user.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    @DisplayName("특정 이메일의 계정 조회")
    void findByEmail() {
        // 계정이 존재하는 경우
        {
            // given
            String email = "이메일 1";
            String password = "비밀번호 1";
            String nickname = "닉네임 1";
            String image = "이미지 1";
            String introduce = "소개 1";
            String refreshToken = "리프레쉬 토큰 1";

            // when
            Account account = accountRepository.findByEmail(email).orElse(null);

            // then
            assertThat(account.getEmail()).isEqualTo(email);
            assertThat(account.getPassword()).isEqualTo(password);
            assertThat(account.getNickname()).isEqualTo(nickname);
            assertThat(account.getImage()).isEqualTo(image);
            assertThat(account.getIntroduce()).isEqualTo(introduce);
            assertThat(account.getRefreshToken()).isEqualTo(refreshToken);
        }
        // 계정이 존재하지 않는 경우
        {
            // given
            String email = "잘못된 이메일";

            // when
            Account account = accountRepository.findByEmail(email).orElse(null);

            // then
            assertThat(account).isEqualTo(null);
        }
    }
}