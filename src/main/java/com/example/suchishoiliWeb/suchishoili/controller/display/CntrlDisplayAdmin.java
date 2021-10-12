package com.example.suchishoiliWeb.suchishoili.controller.display;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CntrlDisplayAdmin {
    private Logger logger = LoggerFactory.getLogger(CntrlDisplayAdmin.class);

    @GetMapping("/admin")
    public String viewAdminPage(Model model) {
        return "admin/index";
    }
}
