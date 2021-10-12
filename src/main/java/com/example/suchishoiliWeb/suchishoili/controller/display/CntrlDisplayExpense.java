package com.example.suchishoiliWeb.suchishoili.controller.display;

import com.example.suchishoiliWeb.suchishoili.principal.AdminPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CntrlDisplayExpense {
    private Logger logger = LoggerFactory.getLogger(CntrlDisplayExpense.class);
    @GetMapping("/expense")
    public String viewExpense(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        model.addAttribute("email", admin.getUsername());
        return "admin/expense";
    }
}
