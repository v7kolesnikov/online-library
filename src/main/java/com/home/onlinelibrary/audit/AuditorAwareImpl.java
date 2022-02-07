package com.home.onlinelibrary.audit;

import com.home.onlinelibrary.domain.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = "";
            Object user = authentication.getPrincipal();
            if (user instanceof User) {
                userName = ((User) user).getUsername();
            } else {
                userName = user.toString();
            }
            return Optional.ofNullable(userName);
        } else {
            return Optional.of("Unknown");
        }
    }
}
