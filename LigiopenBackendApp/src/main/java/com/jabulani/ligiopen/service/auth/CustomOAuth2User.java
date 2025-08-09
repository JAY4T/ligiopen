package com.jabulani.ligiopen.service.auth;

import com.jabulani.ligiopen.entity.user.UserEntity;
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
        super();
        // Copy user fields manually since UserEntity doesn't have a copy constructor
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
        this.setGoogleId(user.getGoogleId());
        this.setGoogleEmail(user.getGoogleEmail());
        this.setEmailVerified(user.isEmailVerified());
        this.setAccountEnabled(user.isAccountEnabled());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setProfilePictureId(user.getProfilePictureId());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setBio(user.getBio());
        this.setPreferredLanguage(user.getPreferredLanguage());
        this.setCreatedAt(user.getCreatedAt());
        this.setUpdatedAt(user.getUpdatedAt());
        this.setFavoritedClubs(user.getFavoritedClubs());
        this.setOwnedClubs(user.getOwnedClubs());
        this.setManagedClubs(user.getManagedClubs());
        this.setRole(user.getRole());
        
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