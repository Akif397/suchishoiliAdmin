package com.example.suchishoiliWeb.suchishoili.service;

import com.example.suchishoiliWeb.suchishoili.DAO.DashboardDao;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryStatus;
import com.example.suchishoiliWeb.suchishoili.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminService {
    public DashboardDao displayAdminDashboard(List<Order> orderList){
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
        DashboardDao dashboardDao = new DashboardDao();
        dashboardDao.setTotalOrder(orderList.size());
        dashboardDao.setConfirmedOrder(confirmedOrderCount);
        dashboardDao.setDeliveredOrder(deliveredOrderCount);
        dashboardDao.setCanceledOrder(canceledOrderCount);
        dashboardDao.setInCourierOrder(courierOrderCount);
        return dashboardDao;
    }
}
