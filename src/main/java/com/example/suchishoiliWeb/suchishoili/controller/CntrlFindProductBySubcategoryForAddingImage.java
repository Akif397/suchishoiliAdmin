package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.DAO.ProductDao;
import com.example.suchishoiliWeb.suchishoili.model.Product;
import com.example.suchishoiliWeb.suchishoili.model.ProductSubcategory;
import com.example.suchishoiliWeb.suchishoili.model.SubcategorySize;
import com.example.suchishoiliWeb.suchishoili.repository.ProductRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductSubcategoryRepository;
import com.example.suchishoiliWeb.suchishoili.service.AdminService;
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
public class CntrlFindProductBySubcategoryForAddingImage {
    private Logger logger = LoggerFactory.getLogger(CntrlFindProductBySubcategoryForAddingImage.class);
    private final ProductSubcategoryRepository productSubcategoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CntrlFindProductBySubcategoryForAddingImage(ProductSubcategoryRepository productSubcategoryRepository,
                                                       ProductRepository productRepository) {
        this.productSubcategoryRepository = productSubcategoryRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/findProductBySubcategoryForAddingImage")
    public @ResponseBody
    List<ProductDao> filteredProductListForAddingImage(HttpServletRequest request, HttpServletResponse response) {
        String subcategory = request.getParameter("subcategory");
        ProductSubcategory ps = productSubcategoryRepository.findBySubCategory(subcategory);
        List<Product> products = productRepository.findByProductSubcategoryAndIs_image_added(ps, false);
//        List<Product> products = productRepository.findByProductSubcategory(ps);
        List<ProductDao> productDaos = new LinkedList<>();
        for (int i = 0; i < products.size(); i++) {
            ProductDao productDao = new ProductDao();
            Product product = products.get(i);
            productDao.setId(product.getId());
            productDao.setName(product.getName());
            productDao.setPrize(product.getPrize());

            List<String> productSizes = new LinkedList<>();
            List<String> productQuantities = new LinkedList<>();
            for (SubcategorySize subcategorySize : ps.getSubcategorySizes()) {
                productSizes.add(subcategorySize.getSize());
            }
            productDao.setSizes(productSizes);
            productDaos.add(productDao);
        }
        return productDaos;
    }
}
