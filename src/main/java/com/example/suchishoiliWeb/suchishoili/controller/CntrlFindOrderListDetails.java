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
import java.util.List;

@Controller
public class CntrlFindOrderListDetails {
    private Logger logger = LoggerFactory.getLogger(CntrlFindOrderListDetails.class);
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    @Autowired
    public CntrlFindOrderListDetails(OrderRepository orderRepository, ProductRepository productRepository,
                                     OrderService orderService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    @GetMapping("/findOrderListDetails")
    public @ResponseBody
    List<OrderDao> findOrderListDetails(HttpServletRequest request, HttpServletResponse response) {
        String selectFilter = request.getParameter("selectFilter").trim();
        List<Product> productList = productRepository.findAll();
        List<OrderDao> orderDaoList = null;
        List<Order> orderList = null;
        LocalDate now = LocalDate.now();
        if (selectFilter.toLowerCase().equals("today")) {
            LocalDateTime startTime = now.atStartOfDay();
            LocalDateTime endTime = now.atTime(23, 59, 59);
            orderList = orderRepository.findByOrderDateAndtimeBetween(startTime, endTime);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        } else if (selectFilter.toLowerCase().equals("yesterday")) {
            LocalDateTime startTime = now.minusDays(1).atStartOfDay();
            LocalDateTime endTime = now.minusDays(1).atTime(23, 59, 59);
            orderList = orderRepository.findByOrderDateAndtimeBetween(startTime, endTime);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        } else if (selectFilter.toLowerCase().equals("this week")) {
            LocalDateTime firstDayOfWeek = now.minusDays(now.getDayOfWeek().getValue()).atStartOfDay();
            LocalDateTime currentTime = LocalDateTime.now();
            orderList = orderRepository.findByOrderDateAndtimeBetween(firstDayOfWeek,
                    currentTime);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        } else if (selectFilter.toLowerCase().equals("last week")) {
            LocalDateTime lastWeekStartDay =
                    now.minusDays(now.getDayOfWeek().getValue()).atStartOfDay().minusDays(7);
            LocalDateTime lastWeekEndDay =
                    LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()).minusDays(1).atTime(23, 59, 59);
            orderList = orderRepository.findByOrderDateAndtimeBetween(lastWeekStartDay,
                    lastWeekEndDay);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        } else if (selectFilter.toLowerCase().equals("this month")) {
            LocalDateTime startDay = now.minusDays(now.getDayOfMonth()).atStartOfDay();
            LocalDateTime currentTime = LocalDateTime.now();
            System.out.println(startDay);
            orderList = orderRepository.findByOrderDateAndtimeBetween(startDay, currentTime);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        } else if (selectFilter.toLowerCase().equals("last month")) {
            int currentMonthValue = now.getMonthValue();
            LocalDateTime endDay = now.minusDays(now.getDayOfMonth() + 1).atTime(23, 59, 59);
            LocalDateTime startDay = null;
            LocalDate previousMonth = now.minusMonths(1);
            startDay = previousMonth.minusDays(previousMonth.getDayOfMonth() - 1).atStartOfDay();
            orderList = orderRepository.findByOrderDateAndtimeBetween(startDay, endDay);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        }
        return orderDaoList;
    }
}
