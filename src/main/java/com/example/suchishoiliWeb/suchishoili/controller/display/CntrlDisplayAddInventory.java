package com.example.suchishoiliWeb.suchishoili.controller.display;

import com.example.suchishoiliWeb.suchishoili.model.Product;
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
public class CntrlDisplayAddInventory {
    private Logger logger = LoggerFactory.getLogger(CntrlDisplayAddInventory.class);
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public CntrlDisplayAddInventory(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @GetMapping("/addInventory")
    public String viewAddInventory(Product product, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        List<ProductCategory> allCategory = productCategoryRepository.findAll();
        model.addAttribute("email", admin.getUsername());
        model.addAttribute("categoryList", allCategory);
        return "admin/addInventory";
    }
}
