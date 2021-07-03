package com.example.suchishoiliWeb.suchishoili.DAO;

import java.util.List;

public class OrderDao {
    private Long orderID;
    private Long ordeUniqueID;
    private String deliveryStatus;
    private String orderNote;
    private String paymentStatus;
    private String paymentMethod;
    private String orderFrom;
    private int orderDiscount;
    private List<ProductDao> productDaos;
    private UserDao userDao;

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public List<ProductDao> getProductDaos() {
        return productDaos;
    }

    public void setProductDaos(List<ProductDao> productDaos) {
        this.productDaos = productDaos;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom;
    }

    public int getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(int orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public Long getOrdeUniqueID() {
        return ordeUniqueID;
    }

    public void setOrdeUniqueID(Long ordeUniqueID) {
        this.ordeUniqueID = ordeUniqueID;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
