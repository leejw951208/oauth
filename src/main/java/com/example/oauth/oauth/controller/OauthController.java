package com.example.oauth.oauth.controller;

import com.example.oauth.jwt.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OauthController {

    @GetMapping("/oauth2/redirect")
    public String oauthRedirect(
            @RequestParam("accessToken") String accessToken,
            @RequestParam("refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        Cookie cookie1 = new Cookie(JwtProperties.ACCESS, accessToken);
        cookie1.setMaxAge(JwtProperties.ACCESS_TOKEN_EXP_TIME);
        cookie1.setPath("/");
        response.addCookie(cookie1);

        Cookie cookie2 = new Cookie(JwtProperties.REFRESH, refreshToken);
        cookie2.setMaxAge(JwtProperties.REFRESH_TOKEN_EXP_TIME);
        cookie2.setPath("/");
        response.addCookie(cookie2);

        return "oauth-redirect";
    }
}
