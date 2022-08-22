package com.astar.gostudy_be.domain.user.service;

import com.astar.gostudy_be.domain.user.dto.ProfileDto;
import com.astar.gostudy_be.domain.user.dto.ProfileUpdateDto;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.astar.gostudy_be.domain.user.dto.AccountAdapter;
import com.astar.gostudy_be.domain.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Transactional
    public Account getAccount(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        return new AccountAdapter(account);
    }

    @Transactional
    public Long join(String email, String password, String nickname, PasswordEncoder passwordEncoder) {
        return accountRepository.save(Account.builder()
                .email(email)
                .image("thumbnail_default.png")
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .introduce("")
                .roles(Collections.singletonList("ROLE_USER"))
                .build()).getId();
    }

    @Transactional
    public Account login(String email, String password, PasswordEncoder passwordEncoder) {
        Account member = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return member;
    }

    @Transactional
    public Long changePassword(String currentPassword, String newPassword, Account account, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return accountRepository.save(account.update(null, passwordEncoder.encode(newPassword), null, null, null, null)).getId();
    }

    @Transactional(readOnly = true)
    public ProfileDto getProfileByAccount(Account account) {
        return new ProfileDto(account.getId(), account.getNickname(), account.getEmail(), account.getImage(), account.getIntroduce());
    }

    @Transactional
    public Long updateProfile(ProfileUpdateDto profileUpdateDto, Account account) {
        String filename = null;
        if(profileUpdateDto.getImage() != null) {
            if (!Objects.equals(account.getImage(), "default.png") || !account.getImage().contains("http")) {
                new File("C://uploads/profile/images/" + account.getImage()).delete();
                new File("C://uploads/profile/thumbnail_images/thumbnail_" + account.getImage()).delete();
            }
            String extension = StringUtils.getFilenameExtension(profileUpdateDto.getImage().getOriginalFilename());
            filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + '.' + extension;

            try {
                File imageFile = new File("C://uploads/profile/images/" + filename);
                profileUpdateDto.getImage().transferTo(imageFile);

                File thumbnailImageFile = new File("C://uploads/profile/thumbnail_images/" + "thumbnail_" + filename);
                Image image = ImageIO.read(imageFile);
                Image resizedImage = image.getScaledInstance(640, 640, Image.SCALE_SMOOTH);
                BufferedImage newImage = new BufferedImage(640, 640, BufferedImage.TYPE_INT_RGB);
                Graphics graphics = newImage.getGraphics();
                graphics.drawImage(resizedImage, 0, 0, null);
                graphics.dispose();
                ImageIO.write(newImage, extension, thumbnailImageFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if(profileUpdateDto.getSetDefaultImage())
        {
            filename = "default.png";
        }
        account.update(profileUpdateDto.getNickname(), null, filename, profileUpdateDto.getIntroduce(), null, null);
        return accountRepository.save(account).getId();
    }
}
