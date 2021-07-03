package com.example.suchishoiliWeb.suchishoili.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.suchishoiliWeb.suchishoili.model.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    Order findByOrderUniqueID(Long orderUniqeID);

    List<Order> findByOrderDateAndtimeBetween(LocalDateTime start, LocalDateTime end);
}
