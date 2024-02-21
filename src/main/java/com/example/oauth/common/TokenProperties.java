package com.example.oauth.common;

public interface TokenProperties {
    String JWT_SECRET = "n93tz90Bm-OK3Bp1om_AYMZKgUnTbiMPaiIAD165lknaopq1SAqAPpcxqaqNYasxcU41RNQ";
    int TOKEN_EXP_3600 = 3600; // 1시간
    int TOKEN_EXP_86400 = 86400; // 1일
    String BEARER = "Bearer ";
    String AUTHORIZATION = "Authorization";
    String ACCESS_TOKEN = "AccessToken";
    String REFRESH_TOKEN = "RefreshToken";
    String PROVIDER = "Provider";
    String PROVIDER_TOKEN = "ProviderToken";
}
