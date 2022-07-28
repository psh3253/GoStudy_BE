package com.astar.gostudy_be.domain.user.service;

import com.astar.gostudy_be.domain.user.entity.Account;
import com.astar.gostudy_be.domain.user.dto.AccountAdapter;
import com.astar.gostudy_be.domain.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
