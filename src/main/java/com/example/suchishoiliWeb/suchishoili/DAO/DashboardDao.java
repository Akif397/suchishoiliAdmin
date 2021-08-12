package com.example.suchishoiliWeb.suchishoili.DAO;

public class DashboardDao {
    private int deliveredOrder;
    private int canceledOrder;
    private int inCourierOrder;
    private int inFactoryOrder;
    private int confirmedOrder;
    private int totalOrder;

    public int getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }

    public int getDeliveredOrder() {
        return deliveredOrder;
    }

    public void setDeliveredOrder(int deliveredOrder) {
        this.deliveredOrder = deliveredOrder;
    }

    public int getCanceledOrder() {
        return canceledOrder;
    }

    public void setCanceledOrder(int canceledOrder) {
        this.canceledOrder = canceledOrder;
    }

    public int getInCourierOrder() {
        return inCourierOrder;
    }

    public void setInCourierOrder(int inCourierOrder) {
        this.inCourierOrder = inCourierOrder;
    }

    public int getInFactoryOrder() {
        return inFactoryOrder;
    }

    public void setInFactoryOrder(int inFactoryOrder) {
        this.inFactoryOrder = inFactoryOrder;
    }

    public int getConfirmedOrder() {
        return confirmedOrder;
    }

    public void setConfirmedOrder(int confirmedOrder) {
        this.confirmedOrder = confirmedOrder;
    }
}
