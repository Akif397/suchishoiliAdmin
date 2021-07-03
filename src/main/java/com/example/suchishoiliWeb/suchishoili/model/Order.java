package com.example.suchishoiliWeb.suchishoili.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 20)
    private int orderQuantity;

    @Column(nullable = false, length = 32)
    private int orderDiscount;

    @Column(nullable = false)
    private boolean paymentStatus;

    @Column(nullable = false, length = 50)
    private String paymentMethod;

    @Column(nullable = false, length = 20)
    private String orderFrom;

    @Column(nullable = false, length = 500)
    private String note;

    @ManyToOne
    private User user;

    @Column(nullable = false, length = 100)
    private Long orderUniqueID;

    @Column(nullable = false, length = 100)
    private String deliveryStatus;

    public LocalDateTime getOrderDateAndtime() {
        return orderDateAndtime;
    }

    public void setOrderDateAndtime(LocalDateTime orderDateAndtime) {
        this.orderDateAndtime = orderDateAndtime;
    }

    @Column(nullable = false, length = 100)
    private LocalDateTime orderDateAndtime;

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Long getOrderUniqueID() {
        return orderUniqueID;
    }

    public void setOrderUniqueID(Long orderUniqueID) {
        this.orderUniqueID = orderUniqueID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public int getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(int orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
