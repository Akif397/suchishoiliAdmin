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
import java.util.List;

@Controller
public class CntrlFindDashboardDetails {
    private Logger logger = LoggerFactory.getLogger(CntrlFindDashboardDetails.class);
    private final AdminService adminService;
    private final OrderRepository orderRepository;

    @Autowired
    public CntrlFindDashboardDetails(AdminService adminService, OrderRepository orderRepository) {
        this.adminService = adminService;
        this.orderRepository = orderRepository;
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
