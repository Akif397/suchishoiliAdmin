package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.DAO.SteadFastResponseDao;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryAgent;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryCharge;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryStatus;
import com.example.suchishoiliWeb.suchishoili.model.*;
import com.example.suchishoiliWeb.suchishoili.repository.*;
import com.example.suchishoiliWeb.suchishoili.service.SteadFastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Controller
public class CntrlSubmitAddOrder {
    private Logger logger = LoggerFactory.getLogger(CntrlSubmitAddOrder.class);
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SubcategorySizeRepository subcategorySizeRepository;
    private final OrderProductSizeQuantityRepository orderProductSizeQuantityRepository;
    private final SteadFastService steadFastService;

    @Autowired
    public CntrlSubmitAddOrder(OrderRepository orderRepository, UserRepository userRepository,
                               ProductRepository productRepository,
                               SubcategorySizeRepository subcategorySizeRepository,
                               OrderProductSizeQuantityRepository orderProductSizeQuantityRepository,
                               SteadFastService steadFastService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.subcategorySizeRepository = subcategorySizeRepository;
        this.orderProductSizeQuantityRepository = orderProductSizeQuantityRepository;
        this.steadFastService = steadFastService;
    }

    @PostMapping("/submitAddOrder")
    public ResponseEntity<String> filteredProductList(HttpServletRequest request) {
        // taking values from request
        String clientName = request.getParameter("clientName").trim();
        String clientPhoneNumber = request.getParameter("clientPhoneNumber").trim();
        String clientLocation = request.getParameter("clientLocation").trim();
        String clientAddress = request.getParameter("clientAddress").trim();
        String clientNote = request.getParameter("clientNote").trim();
        String products = request.getParameter("products");
        String paymentStatus = request.getParameter("paymentStatus").trim();
        String paymentMethod = request.getParameter("paymentMethod").trim();
        int orderDiscount = Integer.parseInt(request.getParameter("orderDiscount"));
        String orderFrom = request.getParameter("orderFrom").trim();
        String orderDeliveryAgent = request.getParameter("orderDeliveryAgent").trim();
        JSONArray productsJSON = new JSONArray(products);
        int orderTotalPrice = Integer.parseInt(request.getParameter("order_total_price"));
        long clientPhoneNumberLong = 0;
        if (clientPhoneNumber.contains("+88")) {
            clientPhoneNumberLong = Long.parseLong(clientPhoneNumber.replace("+88", ""));
        } else {
            clientPhoneNumberLong = Long.parseLong(clientPhoneNumber);
        }

        // order
        Order order = new Order();
        order.setNote(clientNote);
        order.setOrderDiscount(orderDiscount);
        order.setOrderFrom(orderFrom);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(paymentStatus.toLowerCase().equals("paid") ? true : false);
        order.setOrderUniqueID(System.currentTimeMillis() + clientPhoneNumberLong);
        order.setDeliveryStatus(DeliveryStatus.ORDER_TAKEN);
        if (orderDeliveryAgent.toUpperCase().equals(DeliveryAgent.STEADFAST)) {
            order.setOrderDeliveryAgent(DeliveryAgent.STEADFAST);
        } else if (orderDeliveryAgent.toUpperCase().equals(DeliveryAgent.ECOURIER)) {
            order.setOrderDeliveryAgent(DeliveryAgent.ECOURIER);
        }
//
        // total order amount count
        int orderAmount = 0;
        for (int i = 0; i < productsJSON.length(); i++) {
            int productQuantity = Integer.parseInt(String.valueOf(productsJSON.getJSONObject(i).get("quantity")));
            orderAmount += productQuantity;
        }

        order.setOrderQuantity(orderAmount);
        order.setOrderDateAndtime(LocalDateTime.now());
        Order orderFromDB = orderRepository.save(order);
        if (orderFromDB == null) {
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // user
        User userFromDB = userRepository.findByNameAndPhoneNumber(clientName, clientPhoneNumber);
        List<Order> ordersForUser = null;
        if (userFromDB == null) {
            userFromDB = new User();
            userFromDB.setName(clientName);
            userFromDB.setAddressLocation(clientLocation);
            userFromDB.setAddress(clientAddress);
            userFromDB.setPhoneNumber(clientPhoneNumber);
            ordersForUser = new LinkedList<Order>();
            ordersForUser.add(orderFromDB);
            userFromDB.setOrders(ordersForUser);
        } else {
            ordersForUser = userFromDB.getOrders();
            ordersForUser.add(orderFromDB);
        }

        User user = userRepository.saveAndFlush(userFromDB);
        if (user == null) {
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // products
        for (int i = 0; i < productsJSON.length(); i++) {
            Long productID = Long.parseLong(String.valueOf(productsJSON.getJSONObject(i).get("id")));
            String productName = String.valueOf(productsJSON.getJSONObject(i).get("name"));
            String productSize = String.valueOf(productsJSON.getJSONObject(i).get("size"));
            int productQuantity = Integer.parseInt(String.valueOf(productsJSON.getJSONObject(i).get("quantity")));
            int productDiscount = Integer.parseInt(String.valueOf(productsJSON.getJSONObject(i).get("discount")));

            Product product = productRepository.findByIdAndName(productID, productName);
            List<SubcategorySize> subcategorySizeList = product.getProductSubcategory().getSubcategorySizes();
            SubcategorySize subcategorySize = null;
            for (SubcategorySize size : subcategorySizeList) {
                if (size.getSize().toLowerCase().equals(productSize.toLowerCase())) {
                    SubcategorySize sizeFromDB =
                            subcategorySizeRepository.findBySizeAndSubcategoryId(size.getSize(),
                                    product.getProductSubcategory().getId());
                    List<Order> orderList = null;
                    if (sizeFromDB.getOrders().size() > 0) {
                        orderList = sizeFromDB.getOrders();
                        orderList.add(orderFromDB);
                    } else {
                        orderList = new LinkedList<>();
                        orderList.add(orderFromDB);
                    }
                    sizeFromDB.setOrders(orderList);
                    subcategorySize = subcategorySizeRepository.saveAndFlush(sizeFromDB);
                    break;
                }
            }
            if (subcategorySize == null) {
                return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            }
//            Product productFromDB = productRepository.saveAndFlush(product);
//            if (productFromDB == null) {
//                return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
//            }

            OrderProductSizeQuantity orderProductSizeQuantity = new OrderProductSizeQuantity();

            orderProductSizeQuantity.setOrderQuantity(productQuantity);
            orderProductSizeQuantity.setProductSize(productSize);
            orderProductSizeQuantity.setDiscount(productDiscount);
            orderProductSizeQuantity.setProductSizeId(subcategorySize.getId());
            orderProductSizeQuantity.setOrderId(orderFromDB.getId());
            OrderProductSizeQuantity orderProductSizeQuantityFromDB = orderProductSizeQuantityRepository
                    .saveAndFlush(orderProductSizeQuantity);
            if (orderProductSizeQuantityFromDB == null) {
                return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (orderFromDB.isPaymentStatus()) {
            orderTotalPrice = 0;
        } else {
            if (clientLocation.toLowerCase().equals("inside dhaka")) {
                orderTotalPrice = orderTotalPrice + DeliveryCharge.INSIDE_DHAKA;
            } else {
                orderTotalPrice = orderTotalPrice + DeliveryCharge.OUTSIDE_DHAKA;
            }
        }
        if (orderFromDB.getOrderDeliveryAgent().equals(DeliveryAgent.STEADFAST)){
            SteadFastResponseDao steadFastResponseDAO = null;
            try {
                steadFastResponseDAO =
                        steadFastService.order_create(String.valueOf(orderFromDB.getOrderUniqueID()),
                                clientName,
                                clientPhoneNumber, clientAddress, orderTotalPrice, clientNote);
            } catch (JsonProcessingException e) {
                logger.error("Could not parse the json for SteadFast API create_order. " + "(error: " +
                                "{})",
                        e.getMessage());
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (steadFastResponseDAO.getStatus() != 200) {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
            orderFromDB.setDeliveryStatus(steadFastResponseDAO.getConsignment().getStatus().toUpperCase());
            orderFromDB.setUser(user);
            orderRepository.save(orderFromDB);
        }

        return ResponseEntity.ok("1");
    }
}
