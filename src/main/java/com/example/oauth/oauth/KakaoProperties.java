package com.example.oauth.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoProperties(
        String clientId,
        String redirectUri,
        String clientSecret,
        String[] scope
) {
}
