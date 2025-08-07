package com.jabulani.ligiopen.config.security;

import com.jabulani.ligiopen.dao.UserEntityDao;
import com.jabulani.ligiopen.model.tables.UserEntity;
import com.jabulani.ligiopen.model.tables.UserEntity.UserRole;
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
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserEntityDao userEntityDao;

    @Autowired
    public CustomUserDetailsService(UserEntityDao userEntityDao) {
        this.userEntityDao = userEntityDao;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        UserEntity user = null;

        // Try to find by email first, then by username
        Optional<UserEntity> userOptional = userEntityDao.getUserByEmail(usernameOrEmail);
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            userOptional = userEntityDao.getUserByUsername(usernameOrEmail);
            if (userOptional.isPresent()) {
                user = userOptional.get();
            }
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email or username: " + usernameOrEmail);
        }

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