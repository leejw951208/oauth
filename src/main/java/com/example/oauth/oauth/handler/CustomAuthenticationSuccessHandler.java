package com.example.oauth.oauth.handler;

import com.example.oauth.jwt.GeneratedTokenDto;
import com.example.oauth.common.TokenProperties;
import com.example.oauth.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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

        GeneratedTokenDto generatedTokenDto = jwtProvider.generateToken(authentication);

        Cookie accessTokenCookie = new Cookie(TokenProperties.ACCESS_TOKEN, generatedTokenDto.accessToken());
        accessTokenCookie.setMaxAge(TokenProperties.TOKEN_EXP_3600);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie(TokenProperties.REFRESH_TOKEN, generatedTokenDto.refreshToke());
        refreshTokenCookie.setMaxAge(TokenProperties.TOKEN_EXP_86400);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        Cookie providerCookie = new Cookie(TokenProperties.PROVIDER, oAuth2User.getAttribute("provider"));
        providerCookie.setMaxAge(TokenProperties.TOKEN_EXP_86400);
        providerCookie.setPath("/");
        response.addCookie(providerCookie);

        Cookie providerTokenCookie = new Cookie(TokenProperties.PROVIDER_TOKEN, oAuth2User.getAttribute("providerToken"));
        providerTokenCookie.setMaxAge(TokenProperties.TOKEN_EXP_86400);
        providerTokenCookie.setPath("/");
        response.addCookie(providerTokenCookie);

        String url = UriComponentsBuilder.fromUriString("http://localhost:8080/oauth2/redirect")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, url);
    }
}
