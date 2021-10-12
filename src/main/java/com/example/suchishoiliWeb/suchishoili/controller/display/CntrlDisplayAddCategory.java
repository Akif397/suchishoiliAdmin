package com.example.suchishoiliWeb.suchishoili.controller.display;

import com.example.suchishoiliWeb.suchishoili.model.ProductCategory;
import com.example.suchishoiliWeb.suchishoili.principal.AdminPrincipal;
import com.example.suchishoiliWeb.suchishoili.repository.ProductCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CntrlDisplayAddCategory {
    private Logger logger = LoggerFactory.getLogger(CntrlDisplayAddCategory.class);
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public CntrlDisplayAddCategory(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @GetMapping("/addCategory")
    public String viewAddCategory(ProductCategory productCategory, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        model.addAttribute("email", admin.getUsername());
        List<ProductCategory> allCategory = productCategoryRepository.findAll();
        model.addAttribute("categoryList", allCategory);
        return "admin/addCategory";
    }
}
