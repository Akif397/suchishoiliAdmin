package com.example.suchishoiliWeb.suchishoili.controller.display;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CntrlDisplayAdminLogin {
    private Logger logger = LoggerFactory.getLogger(CntrlDisplayAdminLogin.class);

    @GetMapping("/")
    public String adminLogin(String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }
        return "admin/login";
    }
}
