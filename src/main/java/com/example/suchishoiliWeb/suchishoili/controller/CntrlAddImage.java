package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.model.Product;
import com.example.suchishoiliWeb.suchishoili.model.ProductSubcategory;
import com.example.suchishoiliWeb.suchishoili.repository.ProductRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductSubcategoryRepository;
import com.example.suchishoiliWeb.suchishoili.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class CntrlAddImage {
    private Logger logger = LoggerFactory.getLogger(CntrlAddImage.class);
    private final ProductSubcategoryRepository productSubcategoryRepository;
    private final AdminService adminService;
    private final ProductRepository productRepository;

    @Autowired
    public CntrlAddImage(ProductSubcategoryRepository productSubcategoryRepository, AdminService adminService,
                         ProductRepository productRepository){
        this.productSubcategoryRepository = productSubcategoryRepository;
        this.adminService = adminService;
        this.productRepository = productRepository;
    }

    @PostMapping("/addImage")
    public ResponseEntity<String> addImageToProduct(Model model,
                                                    @RequestParam(required = false, value = "category") String category,
                                                    @RequestParam(required = false, value = "subCategory") String subcategory,
                                                    @RequestParam(required = false, value = "product") String productName,
                                                    @RequestParam(required = false, value = "listImage") MultipartFile[] listImage,
                                                    @RequestParam(required = false, value = "detailsImage1") MultipartFile[] detailsImage1,
                                                    @RequestParam(required = false, value = "detailsImage2") MultipartFile[] detailsImage2,
                                                    @RequestParam(required = false, value = "detailsImage3") MultipartFile[] detailsImage3,
                                                    @RequestParam(required = false, value = "detailsImage4") MultipartFile[] detailsImage4
    ) {
        if (category != null && subcategory != null && productName != null && listImage != null && detailsImage1 != null
                && detailsImage2 != null && detailsImage3 != null && detailsImage4 != null) {
            try {
                adminService.saveProductImage(category, subcategory, productName, listImage, detailsImage1,
                        detailsImage2, detailsImage3, detailsImage4);
                ProductSubcategory productSubcategory = productSubcategoryRepository.findBySubCategory(subcategory);
                Product product = productRepository.findByNameAndProductSubcategory(productName, productSubcategory);
                product.setIs_image_added(true);
                productRepository.save(product);
            } catch (IOException e) {
                logger.error("Could not write the images for product. " + "(error:{})", e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return ResponseEntity.ok("1");
    }
}
