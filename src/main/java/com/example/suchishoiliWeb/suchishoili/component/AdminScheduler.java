package com.example.suchishoiliWeb.suchishoili.component;

import com.example.suchishoiliWeb.suchishoili.DAO.SteadFastResponseDAO;
import com.example.suchishoiliWeb.suchishoili.SuchishoiliApplication;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryAgent;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryStatus;
import com.example.suchishoiliWeb.suchishoili.model.Order;
import com.example.suchishoiliWeb.suchishoili.repository.OrderRepository;
import com.example.suchishoiliWeb.suchishoili.service.SteadFastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AdminScheduler {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    SteadFastService steadFastService;

    private Logger logger = LoggerFactory.getLogger(AdminScheduler.class);

    @Scheduled(fixedDelay = 600000)
    public void deliveryStatusSchedulerForSteadFast() {
        System.out.println("SteadFast Scheduler running");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDate.now().minusDays(20).atStartOfDay();
        List<Order> orderList =
                orderRepository.findByDeliveryStatusIsNotAndOrderDateAndtimeBetweenAndOrderDeliveryAgent(
                        DeliveryStatus.DELIVERED, start, now, DeliveryAgent.STEADFAST);
        for (Order order : orderList) {
            SteadFastResponseDAO steadFastResponseDAO = null;
            try {
                steadFastResponseDAO =
                        steadFastService.checking_delivery_status(String.valueOf(order.getOrderUniqueID()));
            } catch (JsonProcessingException e) {
                logger.error("Could not parse the json for SteadFast API create_order. " + "(error: " +
                        "{})", e.getMessage());
            }
//            if (steadFastResponseDAO.getStatus() != 200) {
//                System.out.println("Order ID: " + order.getId());
//                System.out.println(steadFastResponseDAO.getMessage());
//            }
            if(!order.getDeliveryStatus().equals(steadFastResponseDAO.getDelivery_status().toUpperCase())){
                order.setDeliveryStatus(steadFastResponseDAO.getDelivery_status().toUpperCase());
                orderRepository.save(order);
            }
        }
    }
}
