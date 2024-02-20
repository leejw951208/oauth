package com.example.oauth.oauth.handler;

import com.example.oauth.jwt.GeneratedTokenDto;
import com.example.oauth.jwt.JwtProperties;
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
        GeneratedTokenDto generatedTokenDto = jwtProvider.generateToken(authentication);

        String url = UriComponentsBuilder.fromUriString("http://localhost:8080/oauth2/redirect")
                .queryParam("accessToken", generatedTokenDto.accessToken())
                .queryParam("refreshToken", generatedTokenDto.refreshToke())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, url);
    }
}
