package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.DAO.ProductDao;
import com.example.suchishoiliWeb.suchishoili.model.Product;
import com.example.suchishoiliWeb.suchishoili.model.ProductCategory;
import com.example.suchishoiliWeb.suchishoili.model.ProductSubcategory;
import com.example.suchishoiliWeb.suchishoili.model.SubcategorySize;
import com.example.suchishoiliWeb.suchishoili.repository.*;
import com.example.suchishoiliWeb.suchishoili.service.AdminService;
import com.example.suchishoiliWeb.suchishoili.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Controller
@Transactional
public class ProductController {
    @Autowired
    private ProductSubcategoryRepository productSubcategoryRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SubcategorySizeRepository sizeAndQuantityRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductSizeQuantityRepository orderProductSizeQuantityRepository;

    @Autowired
    SubcategorySizeRepository subcategorySizeRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(ProductController.class);

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

    @GetMapping("/findProductBySubcategory")
    public @ResponseBody
    List<ProductDao> filteredProductList(HttpServletRequest request, HttpServletResponse response) {
        String subcategory = request.getParameter("subcategory");
        ProductSubcategory ps = productSubcategoryRepository.findBySubCategory(subcategory);
        List<Product> products = productRepository.findByProductSubcategory(ps);
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
