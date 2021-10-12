package com.example.suchishoiliWeb.suchishoili.controller.display;

import com.example.suchishoiliWeb.suchishoili.DAO.ProductDao;
import com.example.suchishoiliWeb.suchishoili.DAO.SubcategorySizeDao;
import com.example.suchishoiliWeb.suchishoili.model.Product;
import com.example.suchishoiliWeb.suchishoili.model.ProductCategory;
import com.example.suchishoiliWeb.suchishoili.model.SubcategorySize;
import com.example.suchishoiliWeb.suchishoili.principal.AdminPrincipal;
import com.example.suchishoiliWeb.suchishoili.repository.ProductCategoryRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductRepository;
import com.example.suchishoiliWeb.suchishoili.repository.SubcategorySizeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedList;
import java.util.List;

@Controller
public class CntrlDisplayWatchInventory {
    private Logger logger = LoggerFactory.getLogger(CntrlDisplayWatchInventory.class);
    private final ProductRepository productRepository;
    private final SubcategorySizeRepository subcategorySizeRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public CntrlDisplayWatchInventory(ProductRepository productRepository,
                                      SubcategorySizeRepository subcategorySizeRepository,
                                      ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.subcategorySizeRepository = subcategorySizeRepository;
        this.productCategoryRepository = productCategoryRepository;
    }
    @GetMapping("/watchInventory")
    public String viewWatchInventory(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        model.addAttribute("email", admin.getUsername());
        List<Product> products = productRepository.findAll();
        List<ProductDao> productDaos = new LinkedList<>();
        for (Product product : products) {
            List<SubcategorySize> sizes =
                    subcategorySizeRepository.findBySubcategory(product.getProductSubcategory().getId());
            List<SubcategorySizeDao> subcategorySizeDaoList = new LinkedList<>();
            for (SubcategorySize size : sizes) {
                SubcategorySizeDao sizeDao = new SubcategorySizeDao();
                sizeDao.setId(size.getId());
                sizeDao.setSize(size.getSize());
                subcategorySizeDaoList.add(sizeDao);
            }
            ProductDao productDao = new ProductDao();
            productDao.setName(product.getName());
            productDao.setPrize(product.getPrize());
            productDao.setSubcategorySizeDaoList(subcategorySizeDaoList);
            productDaos.add(productDao);
        }
        List<ProductCategory> categories = productCategoryRepository.findAll();
        model.addAttribute("productList", productDaos);
        model.addAttribute("categoryList", categories);
        return "admin/watchInventory";
    }
}
