package com.home.onlinelibrary.repository;

import com.home.onlinelibrary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
