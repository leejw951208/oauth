package com.example.oauth.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OAuthController {

    @GetMapping("/login/oauth2/code/kakao")
    public String oauth(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("code", code);
        return "redirect:/login";
    }
}
