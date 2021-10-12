package com.example.suchishoiliWeb.suchishoili.controller.display;

import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryStatus;
import com.example.suchishoiliWeb.suchishoili.model.Order;
import com.example.suchishoiliWeb.suchishoili.principal.AdminPrincipal;
import com.example.suchishoiliWeb.suchishoili.repository.OrderRepository;
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
public class CntrlDisplayDashboard {
    private Logger logger = LoggerFactory.getLogger(CntrlDisplayDashboard.class);
    private final OrderRepository orderRepository;

    @Autowired
    public CntrlDisplayDashboard(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
}
