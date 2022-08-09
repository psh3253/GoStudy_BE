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

    @Transactional(readOnly = true)
    public ProfileDto getProfileByAccount(Account account) {
        return new ProfileDto(account.getId(), account.getNickname(), account.getEmail(), account.getImage(), account.getIntroduce());
    }

    @Transactional
    public Long updateProfile(ProfileUpdateDto profileUpdateDto, Account account) {
        String filename = null;
        if(profileUpdateDto.getImage() != null) {
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
