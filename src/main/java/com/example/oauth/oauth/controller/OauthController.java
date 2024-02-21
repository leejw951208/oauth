package com.example.oauth.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OauthController {

    @GetMapping("/oauth2/redirect")
    public String oauthRedirect() {
        return "oauth-redirect";
    }
}
