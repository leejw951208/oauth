package com.example.oauth.oauth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OauthController {

    @GetMapping("/oauth2/redirect/{accessToken}/{refreshToken}")
    public String oauthRedirect(@PathVariable String accessToken, @PathVariable String refreshToken, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("accessToken", accessToken);
        redirectAttributes.addFlashAttribute("refreshToken", refreshToken);

        return "redirect:/home";
    }
}
