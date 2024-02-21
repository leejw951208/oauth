package com.example.oauth.security;

import com.example.oauth.common.TokenProperties;
import com.example.oauth.oauth.OAuthEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, Cookie> cookieMap = Arrays.stream(request.getCookies())
                .collect(Collectors.toMap(Cookie::getName, cookie -> cookie));

        String provider = cookieMap.get(TokenProperties.PROVIDER).getValue();
        if ("kakao".equals(provider)) {
            logoutKakao(cookieMap);
        } else {
            logoutGoogle(cookieMap);
        }

        Cookie accessTokenCookie = cookieMap.get(TokenProperties.ACCESS_TOKEN);
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = cookieMap.get(TokenProperties.REFRESH_TOKEN);
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        Cookie providerCookie = cookieMap.get(TokenProperties.PROVIDER);
        providerCookie.setMaxAge(0);
        response.addCookie(providerCookie);

        Cookie providerTokenCookie = cookieMap.get(TokenProperties.PROVIDER_TOKEN);
        providerTokenCookie.setMaxAge(0);
        response.addCookie(providerTokenCookie);

        response.sendRedirect("/login");
    }

    private void logoutKakao(Map<String, Cookie> cookieMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(TokenProperties.AUTHORIZATION, TokenProperties.BEARER + cookieMap.get(TokenProperties.PROVIDER_TOKEN).getValue());
        headers.add("Content-type", "application/x-www-form-urlencoded");

        String url = OAuthEnum.KAKAO_LOGOUT.getValue();

        logout(url, headers, HttpMethod.POST);
    }

    private void logoutGoogle(Map<String, Cookie> cookieMap) {
        String url = OAuthEnum.GOOGLE_LOGOUT.getValue() + cookieMap.get(TokenProperties.PROVIDER_TOKEN).getValue();

        logout(url, new HttpHeaders(), HttpMethod.POST);
    }

    private void logout(String url, HttpHeaders headers, HttpMethod method) {
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(url, method, httpEntity, String.class);
    }
}
