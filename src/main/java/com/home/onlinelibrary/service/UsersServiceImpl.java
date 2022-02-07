package com.home.onlinelibrary.service;

import com.home.onlinelibrary.domain.Role;
import com.home.onlinelibrary.domain.User;
import com.home.onlinelibrary.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository repository, PasswordEncoder passwordEncoder) {
        this.usersRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean addUser(User user){
        User byUser = usersRepository.findByUsername(user.getUsername());

        if(byUser != null){
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        usersRepository.save(user);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByUsername(username);
    }
}
