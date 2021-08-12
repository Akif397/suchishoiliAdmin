package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.DAO.DashboardDao;
import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.AdminType;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryStatus;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.RedisKeys;
import com.example.suchishoiliWeb.suchishoili.model.Admin;
import com.example.suchishoiliWeb.suchishoili.model.Order;
import com.example.suchishoiliWeb.suchishoili.model.Product;
import com.example.suchishoiliWeb.suchishoili.model.ProductCategory;
import com.example.suchishoiliWeb.suchishoili.repository.AdminRepository;
import com.example.suchishoiliWeb.suchishoili.repository.OrderRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductCategoryRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductRepository;
import com.example.suchishoiliWeb.suchishoili.service.AdminService;
import com.example.suchishoiliWeb.suchishoili.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Controller
public class AdminController {
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
    @Qualifier("redisTemplate")
    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/admin")
    public String viewAdminPage(Model model) {
        return "admin/index";
    }

    @GetMapping("/addOrder")
    public String viewAddOrder(ProductCategory category, Model model) {
        List<ProductCategory> allCategory = productCategoryRepository.findAll();
        model.addAttribute("categoryList", allCategory);
        return "admin/addOrder";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        String email = request.getParameter("email").trim();
//        Todo todoFromRedis = (Todo) template.opsForHash().get(REDIS_CACHE_HASH, idLong);
        Object adminRedisObject = (Object) redisTemplate.opsForHash().get(RedisKeys.ADMIN_KEY, email);

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

    @GetMapping("/watchInventory")
    public String viewWatchInventory(Model model) {
        System.out.println("admin watchInventory");
        List<Product> products = productRepository.findAll();
        List<ProductCategory> categories = productCategoryRepository.findAll();
        model.addAttribute("productList", products);
        model.addAttribute("categoryList", categories);
        return "admin/watchInventory";
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
        LocalDate now = LocalDate.now();
        LocalDateTime startTime = now.atStartOfDay();
        LocalDateTime endTime = now.atTime(23, 59, 59);
        List<Order> orderList = orderRepository.findByOrderDateAndtimeBetween(startTime, endTime);
//        List<Order> orderList = orderRepository.findByDeliveryStatusIsNot(DeliveryStatus.DELIVERED);
        List<OrderDao> orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        model.addAttribute("orderList", orderDaoList);
        return "admin/orderList";
    }

    @GetMapping("/login")
    String adminLogin() {
        System.out.println("admin login");
        return "admin/login";
    }

    @GetMapping("/register")
    String adminRegistration() {
        System.out.println("admin registration");
        return "admin/registration";
    }

    @GetMapping("/adminLogout")
    ResponseEntity<String> adminLogout(HttpServletRequest request) {
        String email = request.getParameter("email").trim();
        Admin adminFromDB = adminRepository.findByEmail(email);
        adminFromDB.setLoggedIn(false);
        adminFromDB.setLastLogoutTime(LocalDateTime.now());
        adminRepository.save(adminFromDB);
        return ok("1");
    }

    @GetMapping("/loginAdmin")
    ResponseEntity<String> loginAdmin(HttpServletRequest request) {
        System.out.println("admin Login");
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();
        Admin adminFromDB = adminRepository.findByEmail(email);
        if (adminFromDB == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!adminFromDB.getPassword().equals(password)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        adminFromDB.setLoggedIn(true);
        adminFromDB.setLastLoginTime(LocalDateTime.now());
        adminFromDB = adminRepository.save(adminFromDB);
        redisTemplate.opsForHash().put(RedisKeys.ADMIN_KEY, adminFromDB.getEmail(), adminFromDB);
        return ResponseEntity.ok("1");
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
        admin.setType(AdminType.ADMIN);
        admin.setConfirmed(false);
        admin.setLoggedIn(true);
        admin.setLastLoginTime(LocalDateTime.now());
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

