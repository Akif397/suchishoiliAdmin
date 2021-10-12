package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.model.ProductSubcategory;
import com.example.suchishoiliWeb.suchishoili.model.SubcategorySize;
import com.example.suchishoiliWeb.suchishoili.repository.ProductSubcategoryRepository;
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
public class CntrlProductSizesForInventory {
    private Logger logger = LoggerFactory.getLogger(CntrlProductSizesForInventory.class);
    private final ProductSubcategoryRepository productSubcategoryRepository;

    @Autowired
    public CntrlProductSizesForInventory(ProductSubcategoryRepository productSubcategoryRepository){
        this.productSubcategoryRepository = productSubcategoryRepository;
    }

    @GetMapping("/productSizesForInventory")
    public @ResponseBody
    List<String> productSizesForInventory(HttpServletRequest request,
                                          HttpServletResponse response) {
        String subcategory = request.getParameter("subcategory");
        ProductSubcategory ps = productSubcategoryRepository.findBySubCategory(subcategory);
        List<SubcategorySize> productSizes = ps.getSubcategorySizes();
        List<String> sizeList = new LinkedList<>();
        for (SubcategorySize size : productSizes) {
            sizeList.add(size.getSize());
        }
        return sizeList;
    }
}
