package com.jabulani.ligiopen.config.security;

import com.jabulani.ligiopen.dao.user.UserAccountDao;
import com.jabulani.ligiopen.model.user.Role;
import com.jabulani.ligiopen.model.user.entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAccountDao userAccountDao;
    @Autowired
    public CustomUserDetailsService(UserAccountDao userAccountDao) {
        this.userAccountDao = userAccountDao;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userAccountDao.getUserAccountByEmail(username);
        return new User(user.getEmail(), user.getPassword(), mapRoleToAuthorities(user.getRole()));
    }

    private Collection<GrantedAuthority> mapRoleToAuthorities(Role role) {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }
}
