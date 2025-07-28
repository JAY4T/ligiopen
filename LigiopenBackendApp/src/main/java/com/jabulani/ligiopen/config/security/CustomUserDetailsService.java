package com.jabulani.ligiopen.config.security;

import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.model.UserEntity;
import com.jabulani.ligiopen.model.UserEntity.UserRole;
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

    private final UserEntityDao userEntityDao;

    @Autowired
    public CustomUserDetailsService(UserEntityDao userEntityDao) {
        this.userEntityDao = userEntityDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userEntityDao.getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        if (!user.isAccountEnabled()) {
            throw new UsernameNotFoundException("User account is disabled");
        }

        return new User(
                user.getEmail() != null ? user.getEmail() : user.getGoogleEmail(),
                user.getPassword() != null ? user.getPassword() : "N/A", // For OAuth users without password
                true, true, true, true, // All enabled
                mapRoleToAuthorities(user.getRole())
        );
    }

    private Collection<GrantedAuthority> mapRoleToAuthorities(UserRole role) {
        if (role == null) {
            return Collections.emptyList();
        }
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}