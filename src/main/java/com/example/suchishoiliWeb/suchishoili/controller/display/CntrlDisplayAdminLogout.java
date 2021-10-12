package com.example.suchishoiliWeb.suchishoili.controller.display;

import com.example.suchishoiliWeb.suchishoili.model.Admin;
import com.example.suchishoiliWeb.suchishoili.principal.AdminPrincipal;
import com.example.suchishoiliWeb.suchishoili.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CntrlDisplayAdminLogout {
    private Logger logger = LoggerFactory.getLogger(CntrlDisplayAdminLogout.class);
    private final AdminRepository adminRepository;

    @Autowired
    public CntrlDisplayAdminLogout(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    @GetMapping("/adminLogout")
    String adminLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
            Admin adminFromDB = adminRepository.findByEmail(admin.getUsername());
            adminFromDB.setLoggedIn(false);
            adminRepository.save(adminFromDB);
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}
