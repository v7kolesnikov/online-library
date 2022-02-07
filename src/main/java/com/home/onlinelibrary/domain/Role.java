package com.home.onlinelibrary.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    //todo: добавить роль администратора с возможностью создавать/редактировать книги
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}