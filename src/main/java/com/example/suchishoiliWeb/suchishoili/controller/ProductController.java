package com.example.suchishoiliWeb.suchishoili.controller;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.DAO.ProductDao;
import com.example.suchishoiliWeb.suchishoili.DAO.UserDao;
import com.example.suchishoiliWeb.suchishoili.model.*;
import com.example.suchishoiliWeb.suchishoili.repository.*;
import com.example.suchishoiliWeb.suchishoili.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/admin")
    public String viewAdminPage(Model model) {
//		model.addAttribute("productList", productService.getAllProducts());
        return "admin/index";
    }

    @GetMapping("/addOrder")
    public String viewAddOrder(ProductCategory category, Model model) {
        List<ProductCategory> allCategory = productCategoryRepository.findAll();
        model.addAttribute("categoryList", allCategory);

        System.out.println("admin addOrder");
        return "admin/addOrder";
    }

    @GetMapping("/watchInventory")
    public String viewWatchInventory(Model model) {
        System.out.println("admin watchInventory");
        List<Product> products = productRepository.findAll();
        List<ProductCategory> categories = productCategoryRepository.findAll();
        model.addAttribute("productList", products);
        model.addAttribute("categoryList", categories);
        return "admin/watchInventory";
    }

    @GetMapping("/productSizesForInventory")
    public @ResponseBody
    List<SubcategorySize> productSizesForInventory(HttpServletRequest request,
                                                   HttpServletResponse response) {
        String subcategory = request.getParameter("subcategory");
        ProductSubcategory ps = productSubcategoryRepository.findBySubCategory(subcategory);
        List<SubcategorySize> productSizes = ps.getProductSizesAndQuantities();
        return productSizes;
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
            for (int j = 0; j < product.getSizes().size(); j++) {
                productSizes.add(product.getSizes().get(j).getSize());
            }
            productDao.setSizes(productSizes);
            productDaos.add(productDao);
        }
        return productDaos;
    }

    @GetMapping("/expense")
    public String viewExpense() {
        System.out.println("admin expense");
        return "admin/expense";
    }

    @GetMapping("/addCategory")
    public String viewAddCategory(ProductCategory productCategory, Model model) {
        List<ProductCategory> allCategory = productCategoryRepository.findAll();
        model.addAttribute("categoryList", allCategory);
        System.out.println("admin addCategory");
        return "admin/addCategory";
    }

    @GetMapping("/addInventory")
    public String viewAddInventory(Product product, Model model) {
        List<ProductCategory> allCategory = productCategoryRepository.findAll();
        model.addAttribute("categoryList", allCategory);
        System.out.println("admin addInventory");
        return "admin/addInventory";
    }

    @GetMapping("/orderList")
    public String viewOrderList(Order order, Model model) {
        System.out.println("admin orderList");
        List<Product> productList = productRepository.findAll();
        List<Order> orderList = orderRepository.findAll();
        List<OrderDao> orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        model.addAttribute("orderList", orderDaoList);
        return "admin/orderList";
    }

    @PostMapping("/submitProductSize")
    public @ResponseBody
    SubcategorySize submitProductSize(HttpServletRequest request, HttpServletResponse response) {
        String pSize = request.getParameter("size");
        String pSubcategory = request.getParameter("subcategory");
        ProductSubcategory productSubcategory = productSubcategoryRepository.findBySubCategory(pSubcategory);
        SubcategorySize productSize = new SubcategorySize();
        productSize.setSize(pSize);
        List<SubcategorySize> productSizes = productSubcategory.getProductSizesAndQuantities();
//		ProductSize productSizeFromDB = productSizeRepository.saveAndFlush(productSize);
        productSizes.add(productSize);
        productSubcategory.setProductSizesAndQuantities(productSizes);
        ProductSubcategory productSubcategoryFromDB = productSubcategoryRepository.saveAndFlush(productSubcategory);
        SubcategorySize responsePS = null;
        for (int i = 0; i < productSubcategoryFromDB.getProductSizesAndQuantities().size(); i++) {
            if (productSubcategoryFromDB.getProductSizesAndQuantities().get(i).getSize().equals(pSize)) {
                responsePS = productSubcategoryFromDB.getProductSizesAndQuantities().get(i);
            }
        }
        return responsePS;
    }

    @PostMapping("/submitAddProduct")
    public ResponseEntity<String> submitAddProduct(HttpServletRequest request, HttpServletResponse response) {
        String subCategory = request.getParameter("subCategory");
        String productName = request.getParameter("productName");
        String productDescription = request.getParameter("productDescription");
        String productSize = request.getParameter("productSize");
        int productPrize = Integer.parseInt(request.getParameter("productPrize"));

        ProductSubcategory subCategoryFromDB = productSubcategoryRepository.findBySubCategory(subCategory);
        Product checkProduct = productRepository.findByNameAndProductSubcategory(productName, subCategoryFromDB);

        // check the product with the given sub-category is in the database or not
        if (checkProduct != null) {
            for (int i = 0; i < checkProduct.getSizes().size(); i++) {
                // the product with the same size has already in the database
                if (productSize.equals(checkProduct.getSizes().get(i).getSize())) {
                    return ResponseEntity.ok("0");
                }
            }
            SubcategorySize sizeAndQuantity = null;
            for (int i = 0; i < subCategoryFromDB.getProductSizesAndQuantities().size(); i++) {
                if (productSize.equals(subCategoryFromDB.getProductSizesAndQuantities().get(i).getSize())) {
                    sizeAndQuantity = subCategoryFromDB.getProductSizesAndQuantities().get(i);
                }
            }
            // the product itself is in the database but the size of the product is new. So
            // add the new size with quantity to that product.
            sizeAndQuantity.setSize(productSize);
//			sizeAndQuantity.setQuantity(productQuantity);
            List<SubcategorySize> productSizeAndQuantities = checkProduct.getSizes();
            productSizeAndQuantities.add(sizeAndQuantity);
            Product productFromDB = productRepository.saveAndFlush(checkProduct);
            if (productFromDB != null) {
                return ResponseEntity.ok("1");
            }
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // the product is new
        Product product = new Product();
        product.setName(productName);
        product.setDescription(productDescription);
        product.setPrize(productPrize);
        product.setCreateDate(LocalDateTime.now());
        product.setProductSubcategory(subCategoryFromDB);

        List<SubcategorySize> productSizesAndQuantities = new LinkedList<SubcategorySize>();
        SubcategorySize sizeAndQuantity = null;

        for (int i = 0; i < subCategoryFromDB.getProductSizesAndQuantities().size(); i++) {
            if (productSize.equals(subCategoryFromDB.getProductSizesAndQuantities().get(i).getSize())) {
                sizeAndQuantity = subCategoryFromDB.getProductSizesAndQuantities().get(i);
//				sizeAndQuantity.setQuantity(productQuantity);
                productSizesAndQuantities.add(sizeAndQuantity);
                break;
            }
        }
        product.setSizes(productSizesAndQuantities);

        Product productFromDB = productRepository.saveAndFlush(product);

        if (productFromDB != null) {
            return ResponseEntity.ok("1");
        }
        return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/submitAddCategory")
    public @ResponseBody
    ProductCategory submitAddCategory(HttpServletRequest request, HttpServletResponse response) {
        String categoryName = request.getParameter("category");
        String subCategoryName = request.getParameter("subCategory");

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
            return savedCategory;
        } else {
            List<ProductSubcategory> productSubcategories = checkedPc.getSubCategories();
            for (int i = 0; i < productSubcategories.size(); i++) {
                if (subCategoryName == productSubcategories.get(i).getSubCategory()) {
                    return checkedPc;
                }
            }
            ProductSubcategory ps = new ProductSubcategory();
            ps.setSubCategory(subCategoryName);
            ProductSubcategory psFromDB = productSubcategoryRepository.saveAndFlush(ps);
            productSubcategories.add(psFromDB);

            ProductCategory savedCategory = productCategoryRepository.saveAndFlush(checkedPc);
            return savedCategory;
        }

    }

    @GetMapping("/findSubCategory")
    public @ResponseBody
    List<String> findSubCategory(HttpServletRequest request, HttpServletResponse response) {
        String category = request.getParameter("category");
        List<String> subcategoryList = new LinkedList<>();
        List<ProductSubcategory> ps = productCategoryRepository.findByCategory(category).getSubCategories();
        for(ProductSubcategory subcategory : ps){
            subcategoryList.add(subcategory.getSubCategory());
        }
        return subcategoryList;
    }

    @DeleteMapping("/deleteSubCategory")
    public @ResponseBody
    List<String> deleteSubCategory(HttpServletRequest request, HttpServletResponse response) {
        String subCategory = request.getParameter("subCategory");
        String category = request.getParameter("category");
        productSubcategoryRepository.deleteBySubCategory(subCategory);
        List<String> subcategoryList = new LinkedList<>();
        ProductCategory checkedPc = productCategoryRepository.findByCategory(category);
        List<ProductSubcategory> productSubcategories = checkedPc.getSubCategories();
        for (int i = 0; i < productSubcategories.size(); i++) {
            if (productSubcategories.get(i).getSubCategory().equals(subCategory)) {
                productSubcategories.remove(i);
            }
            else{
                subcategoryList.add(productSubcategories.get(i).getSubCategory());
            }
        }
        return subcategoryList;
    }
}
