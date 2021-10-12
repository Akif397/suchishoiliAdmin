package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.DAO.DashboardDao;
import com.example.suchishoiliWeb.suchishoili.model.Order;
import com.example.suchishoiliWeb.suchishoili.repository.OrderRepository;
import com.example.suchishoiliWeb.suchishoili.service.AdminService;
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
public class CntrlFilteredDateDashboard {
    private Logger logger = LoggerFactory.getLogger(CntrlFilteredDateDashboard.class);
    private final AdminService adminService;
    private final OrderRepository orderRepository;

    @Autowired
    public CntrlFilteredDateDashboard(AdminService adminService, OrderRepository orderRepository) {
        this.adminService = adminService;
        this.orderRepository = orderRepository;
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
}
