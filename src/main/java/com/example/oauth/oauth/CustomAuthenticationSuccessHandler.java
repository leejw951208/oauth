package com.example.oauth.oauth;

import com.example.oauth.jwt.GeneratedTokenDto;
import com.example.oauth.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String provider = oAuth2User.getAttribute("provider");
        boolean isExist = Boolean.TRUE.equals(oAuth2User.getAttribute("exist"));
        String targetUrl = null;

        if (isExist) {
            GeneratedTokenDto generatedTokenDto = jwtProvider.generateToken(authentication);
            targetUrl = UriComponentsBuilder.fromUriString("/home")
                    .queryParam("accessToken", generatedTokenDto.accessToken())
                    .queryParam("refreshToken", generatedTokenDto.refreshToke())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
        } else {
            targetUrl = UriComponentsBuilder.fromUriString("/home")
                    .queryParam("email", email)
                    .queryParam("refreshToken", provider)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
