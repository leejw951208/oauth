package com.example.oauth.jwt;

public interface JwtProperties {
    String SECRET = "n93tz90Bm-OK3Bp1om_AYMZKgUnTbiMPaiIAD165lknaopq1SAqAPpcxqaqNYasxcU41RNQ";
    int ACCESS_TOKEN_EXPIRATION_HOURS = 1;
    int REFRESH_TOKEN_EXPIRATION_HOURS = 168; // 7일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
