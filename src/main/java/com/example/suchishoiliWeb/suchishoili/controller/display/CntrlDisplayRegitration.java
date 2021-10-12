package com.example.suchishoiliWeb.suchishoili.controller.display;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CntrlDisplayRegitration {
    private Logger logger = LoggerFactory.getLogger(CntrlDisplayRegitration.class);

    @GetMapping("/register")
    String adminRegistration() {
        return "admin/registration";
    }
}
