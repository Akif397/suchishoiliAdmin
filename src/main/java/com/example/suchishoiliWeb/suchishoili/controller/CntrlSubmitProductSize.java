package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.model.ProductSubcategory;
import com.example.suchishoiliWeb.suchishoili.model.SubcategorySize;
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
import java.util.List;

@Controller
public class CntrlSubmitProductSize {
    private Logger logger = LoggerFactory.getLogger(CntrlSubmitProductSize.class);
    private final ProductSubcategoryRepository productSubcategoryRepository;

    @Autowired
    public CntrlSubmitProductSize(ProductSubcategoryRepository productSubcategoryRepository) {
        this.productSubcategoryRepository = productSubcategoryRepository;
    }

    @PostMapping("/submitProductSize")
    public @ResponseBody
    SubcategorySize submitProductSize(HttpServletRequest request, HttpServletResponse response) {
        String pSize = request.getParameter("size");
        String pSubcategory = request.getParameter("subcategory");
        ProductSubcategory productSubcategory = productSubcategoryRepository.findBySubCategory(pSubcategory);
        SubcategorySize productSize = new SubcategorySize();
        productSize.setSize(pSize);
        List<SubcategorySize> productSizes = productSubcategory.getSubcategorySizes();
//		ProductSize productSizeFromDB = productSizeRepository.saveAndFlush(productSize);
        productSizes.add(productSize);
        productSubcategory.setSubcategorySizes(productSizes);
        ProductSubcategory productSubcategoryFromDB = productSubcategoryRepository.saveAndFlush(productSubcategory);
        SubcategorySize responsePS = null;
        for (int i = 0; i < productSubcategoryFromDB.getSubcategorySizes().size(); i++) {
            if (productSubcategoryFromDB.getSubcategorySizes().get(i).getSize().equals(pSize)) {
                responsePS = productSubcategoryFromDB.getSubcategorySizes().get(i);
            }
        }
        return responsePS;
    }
}
