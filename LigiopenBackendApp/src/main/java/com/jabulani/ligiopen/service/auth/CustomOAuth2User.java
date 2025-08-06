package com.jabulani.ligiopen.service.auth;

import com.jabulani.ligiopen.model.tables.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2User extends UserEntity implements OAuth2User {

    private final Map<String, Object> attributes;

    public CustomOAuth2User(UserEntity user, Map<String, Object> attributes) {
        super(user);
        // Ensure attributes is never null
        this.attributes = attributes != null ? new HashMap<>(attributes) : new HashMap<>();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (getRole() == null) {
            return Collections.emptyList();
        }
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + getRole().name()));
    }

    @Override
    public String getName() {
        // For JWT generation, use the email as the subject
        String name = getEmail();
        if (name == null) {
            name = getGoogleEmail();
        }
        if (name == null && attributes != null && attributes.containsKey("email")) {
            name = (String) attributes.get("email");
        }
        if (name == null && attributes != null && attributes.containsKey("sub")) {
            name = (String) attributes.get("sub");
        }

        // Fallback to username if available
        if (name == null) {
            name = getUsername();
        }

        return name != null ? name : "unknown";
    }

    // Helper method to safely get attribute values
    public String getAttributeAsString(String key) {
        if (attributes != null && attributes.containsKey(key)) {
            Object value = attributes.get(key);
            return value != null ? value.toString() : null;
        }
        return null;
    }
}