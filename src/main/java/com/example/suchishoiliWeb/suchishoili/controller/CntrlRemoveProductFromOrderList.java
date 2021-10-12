package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.model.Order;
import com.example.suchishoiliWeb.suchishoili.model.SubcategorySize;
import com.example.suchishoiliWeb.suchishoili.repository.SubcategorySizeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Controller
@Transactional
public class CntrlRemoveProductFromOrderList {
    private Logger logger = LoggerFactory.getLogger(CntrlRemoveProductFromOrderList.class);
    private final SubcategorySizeRepository subcategorySizeRepository;

    @Autowired
    public CntrlRemoveProductFromOrderList(SubcategorySizeRepository subcategorySizeRepository) {
        this.subcategorySizeRepository = subcategorySizeRepository;
    }
    @DeleteMapping("/removeProductFromOrderList")
    public ResponseEntity<String> deleteProductFromOrderList(HttpServletRequest request, HttpServletResponse response) {
        Long productSizeID = Long.parseLong(request.getParameter("productSizeID").trim());
        Long orderID = Long.parseLong(request.getParameter("orderID").trim());
        Optional<SubcategorySize> size = subcategorySizeRepository.findById(productSizeID);
        SubcategorySize productSize = size.get();
        List<Order> orderList = productSize.getOrders();
        for (Order order : orderList) {
            if (order.getId() == orderID) {
                orderList.remove(order);
                break;
            }
        }
        subcategorySizeRepository.saveAndFlush(productSize);
        return ResponseEntity.ok("1");
    }
}
