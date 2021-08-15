package com.example.suchishoiliWeb.suchishoili.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.suchishoiliWeb.suchishoili.model.OrderProductSizeQuantity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public interface OrderProductSizeQuantityRepository extends JpaRepository<OrderProductSizeQuantity, Long> {
    public OrderProductSizeQuantity findByOrderIdAndProductSizeId(Long orderID, Long productSizeID);

    public void deleteByProductSizeIdAndOrderId(Long productSizeId, Long orderId);

    public List<OrderProductSizeQuantity> findByOrderId(Long orderId);
}
