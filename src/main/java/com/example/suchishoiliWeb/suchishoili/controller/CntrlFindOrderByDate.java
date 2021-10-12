package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.model.Order;
import com.example.suchishoiliWeb.suchishoili.model.Product;
import com.example.suchishoiliWeb.suchishoili.repository.OrderRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductRepository;
import com.example.suchishoiliWeb.suchishoili.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class CntrlFindOrderByDate {
    private Logger logger = LoggerFactory.getLogger(CntrlFindOrderByDate.class);
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    @Autowired
    public CntrlFindOrderByDate(OrderRepository orderRepository, ProductRepository productRepository,
                                OrderService orderService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    @GetMapping("/findOrderByDate")
    public @ResponseBody
    List<OrderDao> findOrderByDate(HttpServletRequest request, HttpServletResponse response) {
        String filteredDate = request.getParameter("filteredDate").trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start = LocalDate.parse(filteredDate, formatter).atStartOfDay();
        LocalDateTime end = LocalDate.parse(filteredDate, formatter).plusDays(1).atStartOfDay();
        List<Order> orderList = orderRepository.findByOrderDateAndtimeBetween(start, end);
        if (orderList == null) {
            return null;
        }
        List<Product> productList = productRepository.findAll();
        List<OrderDao> orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        return orderDaoList;
    }
}
