package com.home.onlinelibrary.service;

import com.home.onlinelibrary.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {
    boolean addUser(User user);
}
