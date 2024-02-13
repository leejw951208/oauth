package com.example.oauth.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class OAuthController {

    @GetMapping("/login/oauth2/code/kakao")
    public String oauth(@RequestParam("code") String code) {
        System.out.println(code);
        return "login";
    }
}
