package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.model.ProductSubcategory;
import com.example.suchishoiliWeb.suchishoili.repository.ProductCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

@Controller
public class CntrlFindSubcategory{
    private Logger logger = LoggerFactory.getLogger(CntrlFindSubcategory.class);
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public CntrlFindSubcategory(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @GetMapping("/findSubCategory")
    public @ResponseBody
    List<String> findSubCategory(HttpServletRequest request, HttpServletResponse response) {
        String category = request.getParameter("category");
        List<String> subcategoryList = new LinkedList<>();
        List<ProductSubcategory> ps = productCategoryRepository.findByCategory(category).getSubCategories();
        for (ProductSubcategory subcategory : ps) {
            subcategoryList.add(subcategory.getSubCategory());
        }
        return subcategoryList;
    }
}
