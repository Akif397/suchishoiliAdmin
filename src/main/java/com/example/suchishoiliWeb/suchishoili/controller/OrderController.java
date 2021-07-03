package com.example.suchishoiliWeb.suchishoili.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryStatus;
import com.example.suchishoiliWeb.suchishoili.model.*;
import com.example.suchishoiliWeb.suchishoili.repository.*;
import com.example.suchishoiliWeb.suchishoili.service.OrderService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
public class OrderController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductSizeQuantityRepository orderProductSizeQuantityRepository;
    @Autowired
    SubcategorySizeRepository subcategorySizeRepository;
    @Autowired
    OrderService orderService;

    @GetMapping("/cancelOrder")
    public ResponseEntity<String> cancelOrder(HttpServletRequest request) {
        Long orderUniqeID = Long.parseLong(request.getParameter("orderUniqeID").trim());
        Order order = orderRepository.findByOrderUniqueID(orderUniqeID);
        order.setDeliveryStatus(DeliveryStatus.ORDER_CANCEL);
//
//        Optional<SubcategorySize> size = subcategorySizeRepository.findById(productSizeID);
//        SubcategorySize productSize = size.get();
//        List<Order> orderList = productSize.getOrders();
//        for(Order order : orderList){
//            if (order.getId() == orderID){
//                orderList.remove(order);
//                break;
//            }
//        }
        orderRepository.saveAndFlush(order);
        return ResponseEntity.ok("1");
    }

    @DeleteMapping("/removeProductFromOrderList")
    public ResponseEntity<String> deleteProductFromOrderList(HttpServletRequest request, HttpServletResponse response) {
        Long productSizeID = Long.parseLong(request.getParameter("productSizeID").trim());
        Long orderID = Long.parseLong(request.getParameter("orderID").trim());
        Optional<SubcategorySize> size = subcategorySizeRepository.findById(productSizeID);
        SubcategorySize productSize = size.get();
        List<Order> orderList = productSize.getOrders();
        for(Order order : orderList){
            if (order.getId() == orderID){
                orderList.remove(order);
                break;
            }
        }
        subcategorySizeRepository.saveAndFlush(productSize);
        return ResponseEntity.ok("1");
    }

    @GetMapping("/findOrderByDate")
    public @ResponseBody
    List<OrderDao> findOrderByDate(HttpServletRequest request, HttpServletResponse response) {
        String filteredDate = request.getParameter("filteredDate").trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start = LocalDate.parse(filteredDate, formatter).atStartOfDay();
        LocalDateTime end = LocalDate.parse(filteredDate, formatter).plusDays(1).atStartOfDay();
        List<Order> orderList = orderRepository.findByOrderDateAndtimeBetween(start, end);
        if (orderList == null) {
            return null;
        }
        List<Product> productList = productRepository.findAll();
        List<OrderDao> orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        return orderDaoList;
    }

    @GetMapping("findOrderByOrderID")
    public @ResponseBody
    List<OrderDao> findOrderByOrderID(HttpServletRequest request, HttpServletResponse response) {
        Long filteredOrderID = Long.parseLong(request.getParameter("filteredOrderID").trim());
        List<Order> orderList = null;
        Order orderFromDB = orderRepository.findByOrderUniqueID(filteredOrderID);
        if (orderFromDB != null) {
            orderList = new LinkedList<>();
            orderList.add(orderFromDB);
        } else {
            return null;
        }
        List<Product> productList = productRepository.findAll();
        List<OrderDao> orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        return orderDaoList;
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
        JSONArray productsJSON = new JSONArray(products);
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
            List<SubcategorySize> subcategorySizeList = product.getSizes();
            SubcategorySize subcategorySize = null;
            for (SubcategorySize size : subcategorySizeList) {
                if (size.getSize().toLowerCase().equals(productSize.toLowerCase())) {
                    SubcategorySize sizeFromDB = subcategorySizeRepository.findBySize(size.getSize());
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

        return ResponseEntity.ok("1");
    }

}
