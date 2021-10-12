package com.example.suchishoiliWeb.suchishoili.controller.display;

import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.model.Order;
import com.example.suchishoiliWeb.suchishoili.model.Product;
import com.example.suchishoiliWeb.suchishoili.principal.AdminPrincipal;
import com.example.suchishoiliWeb.suchishoili.repository.OrderRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductRepository;
import com.example.suchishoiliWeb.suchishoili.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CntrlDisplayOrderList {
    private Logger logger = LoggerFactory.getLogger(CntrlDisplayOrderList.class);
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Autowired
    public CntrlDisplayOrderList(ProductRepository productRepository, OrderRepository orderRepository,
                                 OrderService orderService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
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
}
