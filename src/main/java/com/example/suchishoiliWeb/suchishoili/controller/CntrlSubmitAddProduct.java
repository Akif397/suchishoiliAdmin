package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.model.Product;
import com.example.suchishoiliWeb.suchishoili.model.ProductSubcategory;
import com.example.suchishoiliWeb.suchishoili.repository.ProductRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductSubcategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Controller
public class CntrlSubmitAddProduct {
    private Logger logger = LoggerFactory.getLogger(CntrlSubmitAddProduct.class);
    private final ProductSubcategoryRepository productSubcategoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CntrlSubmitAddProduct(ProductSubcategoryRepository productSubcategoryRepository,
                                 ProductRepository productRepository) {
        this.productSubcategoryRepository = productSubcategoryRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/submitAddProduct")
    public ResponseEntity<String> submitAddProduct(HttpServletRequest request, HttpServletResponse response) {
        String subCategory = request.getParameter("subCategory");
        String productName = request.getParameter("productName");
        String productDescription = request.getParameter("productDescription");
        int productPrize = Integer.parseInt(request.getParameter("productPrize"));

        ProductSubcategory subCategoryFromDB = productSubcategoryRepository.findBySubCategory(subCategory);
        Product checkProduct = productRepository.findByNameAndProductSubcategory(productName, subCategoryFromDB);
        // check the product with the given sub-category is in the database or not
        if (checkProduct != null) {
            return new ResponseEntity<String>(HttpStatus.CONFLICT);
        }
        // the product is new
        Product product = new Product();
        product.setName(productName);
        product.setDescription(productDescription);
        product.setPrize(productPrize);
        product.setCreateDate(LocalDateTime.now());
        product.setProductSubcategory(subCategoryFromDB);
        product.setIs_image_added(false);

        Product productFromDB = productRepository.saveAndFlush(product);

        if (productFromDB != null) {
            return ResponseEntity.ok("1");
        }
        return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
