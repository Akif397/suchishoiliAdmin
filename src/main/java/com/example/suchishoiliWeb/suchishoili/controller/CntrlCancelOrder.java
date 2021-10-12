package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryStatus;
import com.example.suchishoiliWeb.suchishoili.model.Order;
import com.example.suchishoiliWeb.suchishoili.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CntrlCancelOrder {
    private Logger logger = LoggerFactory.getLogger(CntrlCancelOrder.class);
    private final OrderRepository orderRepository;

    @Autowired
    public CntrlCancelOrder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/cancelOrder")
    public ResponseEntity<String> cancelOrder(HttpServletRequest request) {
        Long orderUniqeID = Long.parseLong(request.getParameter("orderUniqeID").trim());
        Order order = orderRepository.findByOrderUniqueID(orderUniqeID);
        order.setDeliveryStatus(DeliveryStatus.ORDER_CANCEL);
        orderRepository.saveAndFlush(order);
        return ResponseEntity.ok("1");
    }
}
