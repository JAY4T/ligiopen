package com.jabulani.ligiopen.service.auth;

import com.jabulani.ligiopen.config.security.CustomUserDetailsService;
import com.jabulani.ligiopen.config.security.JWTGenerator;
import com.jabulani.ligiopen.model.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTGenerator jwtGenerator;
    private final CustomUserDetailsService userDetailsService;

    public OAuth2AuthenticationSuccessHandler(JWTGenerator jwtGenerator, 
                                             CustomUserDetailsService userDetailsService) {
        this.jwtGenerator = jwtGenerator;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      Authentication authentication) throws IOException, ServletException {
        
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(oauthUser.getEmail());
        
        String token = jwtGenerator.generateToken(authentication);
        
        String redirectUrl = UriComponentsBuilder.fromUriString("/oauth2/redirect")
            .queryParam("token", token)
            .queryParam("userId", user.getId())
            .build().toUriString();
        
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}