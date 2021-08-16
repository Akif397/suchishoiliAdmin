package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.DAO.DashboardDao;
import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.DAO.ProductDao;
import com.example.suchishoiliWeb.suchishoili.DAO.SubcategorySizeDao;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.AdminFixedValue;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryStatus;
import com.example.suchishoiliWeb.suchishoili.model.*;
import com.example.suchishoiliWeb.suchishoili.principal.AdminPrincipal;
import com.example.suchishoiliWeb.suchishoili.repository.*;
import com.example.suchishoiliWeb.suchishoili.service.AdminService;
import com.example.suchishoiliWeb.suchishoili.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Controller
@ComponentScan(basePackages = {"com.example.suchishoiliWeb.suchishoili.service"})
public class AdminController implements ErrorController {
    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    AdminService adminService;

    @Autowired
    SubcategorySizeRepository subcategorySizeRepository;

//    @Qualifier("redisTemplate")
//    @Autowired
//    RedisTemplate redisTemplate;

    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/admin")
    public String viewAdminPage(Model model) {
        return "admin/index";
    }

    @GetMapping("/addOrder")
    public String viewAddOrder(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        model.addAttribute("email", admin.getUsername());
        List<ProductCategory> allCategory = productCategoryRepository.findAll();
        model.addAttribute("categoryList", allCategory);
        return "admin/addOrder";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
//        String email = request.getParameter("email").trim();
//        Todo todoFromRedis = (Todo) template.opsForHash().get(REDIS_CACHE_HASH, idLong);
//        Object adminRedisObject = (Object) redisTemplate.opsForHash().get(RedisKeys.ADMIN_KEY, email);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        model.addAttribute("email", admin.getUsername());

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().plusDays(1).atStartOfDay();
        List<Order> orderList = orderRepository.findByOrderDateAndtimeBetween(start, end);
        int deliveredOrderCount = 0;
        int canceledOrderCount = 0;
        int courierOrderCount = 0;
        int factoryOrderCount = 0;
        int confirmedOrderCount = 0;
        for (Order order : orderList) {
            if (order.getDeliveryStatus().trim().equals(DeliveryStatus.DELIVERED)) {
                deliveredOrderCount++;
            }
            if (order.getDeliveryStatus().trim().equals(DeliveryStatus.ORDER_CANCEL)) {
                canceledOrderCount++;
            }
            if (order.getDeliveryStatus().trim().equals(DeliveryStatus.IN_COURIER)) {
                courierOrderCount++;
            }
            if (order.getDeliveryStatus().trim().equals(DeliveryStatus.IN_THE_FACTORY)) {
                factoryOrderCount++;
            }
            if (order.getDeliveryStatus().trim().equals(DeliveryStatus.ORDER_TAKEN)) {
                confirmedOrderCount++;
            }
        }
        model.addAttribute("totalOrder", orderList.size());
        model.addAttribute("deliveredOrder", deliveredOrderCount);
        model.addAttribute("canceledOrder", canceledOrderCount);
        model.addAttribute("inCourierOrder", courierOrderCount);
        model.addAttribute("inFactoryOrder", factoryOrderCount);
        model.addAttribute("confirmedOrder", confirmedOrderCount);
        return "admin/adminDashboard";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//
//            if (statusCode == HttpStatus.NOT_FOUND.value()) {
//                return "error-404";
//            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                return "error-500";
//            }
//        }
        return "admin/error";
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

    @GetMapping("/expense")
    public String viewExpense(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        model.addAttribute("email", admin.getUsername());
        return "admin/expense";
    }

    @GetMapping("/addCategory")
    public String viewAddCategory(ProductCategory productCategory, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        model.addAttribute("email", admin.getUsername());
        List<ProductCategory> allCategory = productCategoryRepository.findAll();
        model.addAttribute("categoryList", allCategory);
        return "admin/addCategory";
    }

    @GetMapping("/addImage")
    public String viewAddImage(ProductCategory productCategory, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        model.addAttribute("email", admin.getUsername());
        List<ProductCategory> allCategory = productCategoryRepository.findAll();
        model.addAttribute("categoryList", allCategory);
        return "admin/addImage";
    }

    @GetMapping("/addInventory")
    public String viewAddInventory(Product product, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        List<ProductCategory> allCategory = productCategoryRepository.findAll();
        model.addAttribute("email", admin.getUsername());
        model.addAttribute("categoryList", allCategory);
        System.out.println("admin addInventory");
        return "admin/addInventory";
    }

    @GetMapping("/orderList")
    public String viewOrderList(Order order, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
        model.addAttribute("email", admin.getUsername());
        List<Product> productList = productRepository.findAll();
        LocalDate now = LocalDate.now();
        LocalDateTime startTime = now.atStartOfDay();
        LocalDateTime endTime = now.atTime(23, 59, 59);
        List<Order> orderList = orderRepository.findByOrderDateAndtimeBetween(startTime, endTime);
//        List<Order> orderList = orderRepository.findByDeliveryStatusIsNot(DeliveryStatus.DELIVERED);
        List<OrderDao> orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        model.addAttribute("orderList", orderDaoList);
        return "admin/orderList";
    }

    @GetMapping("/")
    public String adminLogin(String error, Model model) {
        System.out.println("admin login");
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }
        return "admin/login";
    }

    @GetMapping("/register")
    String adminRegistration() {
        System.out.println("admin registration");
        return "admin/registration";
    }

    @GetMapping("/adminLogout")
    String adminLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            AdminPrincipal admin = (AdminPrincipal) auth.getPrincipal();
            Admin adminFromDB = adminRepository.findByEmail(admin.getUsername());
            adminFromDB.setLoggedIn(false);
            adminRepository.save(adminFromDB);
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @PostMapping("/registerAdmin")
    ResponseEntity<String> registerAdmin(HttpServletRequest request) {
        String name = request.getParameter("name").trim();
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();
        if (adminRepository.findByEmail(email) != null) {
            //return 2 if another admin found with the same email
            return ok("2");
        }
        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setType(AdminFixedValue.ADMIN_TYPE);
        admin.setConfirmed(true);
        admin.setLoggedIn(true);
        adminRepository.save(admin);
        return ok("1");
    }

    @GetMapping("/filteredDateDashboard")
    public @ResponseBody
    DashboardDao filteredDateDashboard(HttpServletRequest request, HttpServletResponse response) {
        String filteredDate = request.getParameter("filteredDate").trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start = LocalDate.parse(filteredDate, formatter).atStartOfDay();
        LocalDateTime end = LocalDate.parse(filteredDate, formatter).plusDays(1).atStartOfDay();
        List<Order> orderList = orderRepository.findByOrderDateAndtimeBetween(start, end);
        DashboardDao dashboardDao = adminService.displayAdminDashboard(orderList);
        return dashboardDao;
    }

    @GetMapping("/findDashboardDetails")
    public @ResponseBody
    DashboardDao findDashboardDetails(HttpServletRequest request, HttpServletResponse response) {
        String selectedDate = request.getParameter("selectedDate").trim();
        List<Order> orderList = null;
        DashboardDao dashboardDao = null;
        LocalDate now = LocalDate.now();
        if (selectedDate.toLowerCase().equals("today")) {
            LocalDateTime startTime = now.atStartOfDay();
            LocalDateTime endTime = now.atTime(23, 59, 59);
            orderList = orderRepository.findByOrderDateAndtimeBetween(startTime, endTime);
            dashboardDao = adminService.displayAdminDashboard(orderList);
        } else if (selectedDate.toLowerCase().equals("yesterday")) {
            LocalDateTime startTime = now.minusDays(1).atStartOfDay();
            LocalDateTime endTime = now.minusDays(1).atTime(23, 59, 59);
            orderList = orderRepository.findByOrderDateAndtimeBetween(startTime, endTime);
            dashboardDao = adminService.displayAdminDashboard(orderList);
        } else if (selectedDate.toLowerCase().equals("this week")) {
            LocalDateTime firstDayOfWeek = now.minusDays(now.getDayOfWeek().getValue()).atStartOfDay();
            LocalDateTime currentTime = LocalDateTime.now();
            orderList = orderRepository.findByOrderDateAndtimeBetween(firstDayOfWeek,
                    currentTime);
            dashboardDao = adminService.displayAdminDashboard(orderList);
        } else if (selectedDate.toLowerCase().equals("last week")) {
            LocalDateTime lastWeekStartDay =
                    now.minusDays(now.getDayOfWeek().getValue()).atStartOfDay().minusDays(7);
            LocalDateTime lastWeekEndDay =
                    LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()).minusDays(1).atTime(23, 59, 59);
            orderList = orderRepository.findByOrderDateAndtimeBetween(lastWeekStartDay,
                    lastWeekEndDay);
            dashboardDao = adminService.displayAdminDashboard(orderList);
        } else if (selectedDate.toLowerCase().equals("this month")) {
            LocalDateTime startDay = now.minusDays(now.getDayOfMonth()).atStartOfDay();
            LocalDateTime currentTime = LocalDateTime.now();
            System.out.println(startDay);
            orderList = orderRepository.findByOrderDateAndtimeBetween(startDay, currentTime);
            dashboardDao = adminService.displayAdminDashboard(orderList);
        } else if (selectedDate.toLowerCase().equals("last month")) {
            int currentMonthValue = now.getMonthValue();
            LocalDateTime endDay = now.minusDays(now.getDayOfMonth() + 1).atTime(23, 59, 59);
            LocalDateTime startDay = null;
            LocalDate previousMonth = now.minusMonths(1);
            startDay = previousMonth.minusDays(previousMonth.getDayOfMonth() - 1).atStartOfDay();
            orderList = orderRepository.findByOrderDateAndtimeBetween(startDay, endDay);
            dashboardDao = adminService.displayAdminDashboard(orderList);
        }
        return dashboardDao;
    }
}

