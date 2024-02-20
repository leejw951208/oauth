package com.example.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }
}
