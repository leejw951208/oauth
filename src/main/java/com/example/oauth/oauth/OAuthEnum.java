package com.example.oauth.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthEnum {
    KAKAO_LOGOUT("https://kapi.kakao.com/v1/user/logout"),
    GOOGLE_LOGOUT("https://accounts.google.com/o/oauth2/revoke?token=");

    private final String value;
}
