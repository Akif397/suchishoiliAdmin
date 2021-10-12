package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.model.ProductCategory;
import com.example.suchishoiliWeb.suchishoili.model.ProductSubcategory;
import com.example.suchishoiliWeb.suchishoili.repository.ProductCategoryRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductSubcategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

@Controller
public class CntrlSubmitAddCategory {
    private Logger logger = LoggerFactory.getLogger(CntrlSubmitAddCategory.class);
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public CntrlSubmitAddCategory(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @PostMapping("/submitAddCategory")
    public @ResponseBody
    List<String> submitAddCategory(HttpServletRequest request, HttpServletResponse response) {
        String categoryName = request.getParameter("category");
        String subCategoryName = request.getParameter("subCategory");
        List<String> categoryList = null;

        ProductCategory checkedPc = productCategoryRepository.findByCategory(categoryName);
        if (checkedPc == null) {
            ProductCategory pc = new ProductCategory();
            pc.setCategory(categoryName);

            List<ProductSubcategory> productSubcategories = new LinkedList<ProductSubcategory>();
            ProductSubcategory ps = new ProductSubcategory();
            ps.setSubCategory(subCategoryName);
            productSubcategories.add(ps);

            pc.setSubCategories(productSubcategories);
            ProductCategory savedCategory = productCategoryRepository.saveAndFlush(pc);
            categoryList = new LinkedList<>();
            categoryList.add(savedCategory.getCategory());
            return categoryList;
        } else {
            List<ProductSubcategory> productSubcategories = checkedPc.getSubCategories();
            //subcategory is already in DB
            for (int i = 0; i < productSubcategories.size(); i++) {
                if (subCategoryName.equals(productSubcategories.get(i).getSubCategory())) {
                    categoryList = new LinkedList<>();
                    categoryList.add(checkedPc.getCategory());
                    return categoryList;
                }
            }
            //new subcategory
            ProductSubcategory ps = new ProductSubcategory();
            ps.setSubCategory(subCategoryName);
            productSubcategories.add(ps);

            ProductCategory savedCategory = productCategoryRepository.saveAndFlush(checkedPc);
            categoryList = new LinkedList<>();
            categoryList.add(savedCategory.getCategory());
            return categoryList;
        }

    }
}
