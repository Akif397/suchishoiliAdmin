package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.model.ProductCategory;
import com.example.suchishoiliWeb.suchishoili.model.ProductSubcategory;
import com.example.suchishoiliWeb.suchishoili.repository.ProductCategoryRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductSubcategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

@Controller
public class CntrlDeleteSubCategory {
    private Logger logger = LoggerFactory.getLogger(CntrlDeleteSubCategory.class);
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductSubcategoryRepository productSubcategoryRepository;

    @Autowired
    public CntrlDeleteSubCategory(ProductCategoryRepository productCategoryRepository,
                                  ProductSubcategoryRepository productSubcategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productSubcategoryRepository = productSubcategoryRepository;
    }

    @DeleteMapping("/deleteSubCategory")
    public @ResponseBody
    List<String> deleteSubCategory(HttpServletRequest request, HttpServletResponse response) {
        List<String> subcategoryList = new LinkedList<>();
        String subCategory = request.getParameter("subCategory");
        String category = request.getParameter("category");
        productSubcategoryRepository.deleteBySubCategory(subCategory);
        ProductCategory checkedPc = productCategoryRepository.findByCategory(category);
        List<ProductSubcategory> productSubcategories = checkedPc.getSubCategories();
        for (int i = 0; i < productSubcategories.size(); i++) {
            if (productSubcategories.get(i).getSubCategory().equals(subCategory)) {
                productSubcategories.remove(i);
            }
        }
        ProductCategory productCategory = productCategoryRepository.save(checkedPc);
        for (ProductSubcategory subcategory : productCategory.getSubCategories()) {
            subcategoryList.add(subcategory.getSubCategory());
        }
        return subcategoryList;
    }
}
