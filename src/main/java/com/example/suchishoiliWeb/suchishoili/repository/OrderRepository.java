package com.example.suchishoiliWeb.suchishoili.repository;

import com.example.suchishoiliWeb.suchishoili.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderUniqueID(Long orderUniqeID);

    List<Order> findByDeliveryStatusIsNotAndOrderDateAndtimeBetweenAndOrderDeliveryAgent(
            String deliveryStatus, LocalDateTime start, LocalDateTime end, String orderDeliveryAgent);

    List<Order> findByOrderDateAndtimeBetween(LocalDateTime start, LocalDateTime end);

    //    @Query(value = "SELECT * FROM products p WHERE NOT p.id = ?1", nativeQuery = true)
    List<Order> findByDeliveryStatusIsNot(String deliveryStatus);
//    List<Order> findByOrderDateAndtime
}
