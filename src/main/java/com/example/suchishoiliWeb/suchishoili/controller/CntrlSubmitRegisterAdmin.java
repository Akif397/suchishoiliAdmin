package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.fixedVariables.AdminFixedValue;
import com.example.suchishoiliWeb.suchishoili.model.Admin;
import com.example.suchishoiliWeb.suchishoili.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.ResponseEntity.ok;

@Controller
public class CntrlSubmitRegisterAdmin {
    private Logger logger = LoggerFactory.getLogger(CntrlSubmitRegisterAdmin.class);
    private final AdminRepository adminRepository;

    @Autowired
    public CntrlSubmitRegisterAdmin(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    @PostMapping("/registerAdmin")
    ResponseEntity<String> registerAdmin(HttpServletRequest request) {
        String name = request.getParameter("name").trim();
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();
        if (adminRepository.findByEmail(email) != null) {
            //return 2 if another admin found with the same email
            return ok("2");
        }
        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setType(AdminFixedValue.ADMIN_TYPE);
        admin.setConfirmed(true);
        admin.setLoggedIn(true);
        adminRepository.save(admin);
        return ok("1");
    }
}
