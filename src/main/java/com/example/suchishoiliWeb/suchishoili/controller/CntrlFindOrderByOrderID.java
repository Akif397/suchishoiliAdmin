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
import java.util.LinkedList;
import java.util.List;

@Controller
public class CntrlFindOrderByOrderID {
    private Logger logger = LoggerFactory.getLogger(CntrlFindOrderByOrderID.class);
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    @Autowired
    public CntrlFindOrderByOrderID(OrderRepository orderRepository, ProductRepository productRepository,
                                     OrderService orderService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    @GetMapping("findOrderByOrderID")
    public @ResponseBody
    List<OrderDao> findOrderByOrderID(HttpServletRequest request, HttpServletResponse response) {
        Long filteredOrderID = Long.parseLong(request.getParameter("filteredOrderID").trim());
        List<Order> orderList = null;
        Order orderFromDB = orderRepository.findByOrderUniqueID(filteredOrderID);
        if (orderFromDB != null) {
            orderList = new LinkedList<>();
            orderList.add(orderFromDB);
        } else {
            return null;
        }
        List<Product> productList = productRepository.findAll();
        List<OrderDao> orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        return orderDaoList;
    }
}
