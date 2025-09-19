package com.hieu.jobhunter.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hieu.jobhunter.domain.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.hieu.jobhunter.domain.User user = this.userService.handleFindUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return new CustomUserDetails(user); // trả về CustomUserDetails thay vì Spring User
    }

}
