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
        // Try to find user by email first, then by username
        Optional<UserEntity> userOptional = userEntityDao.getUserByEmail(usernameOrEmail);

        if (userOptional.isEmpty()) {
            userOptional = userEntityDao.getUserByUsername(usernameOrEmail);
        }

        UserEntity user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email or username: " + usernameOrEmail));

        if (!user.isAccountEnabled()) {
            throw new UsernameNotFoundException("User account is disabled");
        }

        // Use email as the principal if available, otherwise use username
        String principal = user.getEmail() != null ? user.getEmail() :
                (user.getGoogleEmail() != null ? user.getGoogleEmail() : user.getUsername());

        return new User(
                principal,
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