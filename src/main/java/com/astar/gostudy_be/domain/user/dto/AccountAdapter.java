package com.astar.gostudy_be.domain.user.dto;

import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class AccountAdapter extends User {
    private Account account;

    public AccountAdapter(Account account) {
        super(account.getEmail(), account.getPassword() != null ? account.getPassword() : "", Collections.singleton(new SimpleGrantedAuthority(account.getRoles().get(0))));
        this.account = account;
    }
}
