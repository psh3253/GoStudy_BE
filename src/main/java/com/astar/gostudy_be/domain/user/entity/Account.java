package com.astar.gostudy_be.domain.user.entity;

import com.astar.gostudy_be.domain.BaseTimeEntity;
import com.astar.gostudy_be.domain.study.entity.Category;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String nickname;

    @Column
    @NotNull
    private String email;

    @Column
    private String password;

    @Column
    private String image;

    @Column
    private String introduce;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Column
    private String refreshToken;

    public Account update(String nickname, String password, String image, String introduce, List<String> roles, String refreshToken) {
        if(nickname != null)
            this.nickname = nickname;
        if(password != null)
            this.password = password;
        if(image != null)
            this.image = image;
        if(introduce != null)
            this.introduce = introduce;
        if(roles != null)
            this.roles = roles;
        if(refreshToken != null)
            this.refreshToken = refreshToken;
        return this;
    }
}
