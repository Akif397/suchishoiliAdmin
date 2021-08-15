package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.DAO.SteadFastResponseDAO;
import com.example.suchishoiliWeb.suchishoili.SuchishoiliApplication;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryAgent;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryCharge;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryStatus;
import com.example.suchishoiliWeb.suchishoili.model.*;
import com.example.suchishoiliWeb.suchishoili.repository.*;
import com.example.suchishoiliWeb.suchishoili.service.EXCELGeneratorService;
import com.example.suchishoiliWeb.suchishoili.service.OrderService;
import com.example.suchishoiliWeb.suchishoili.service.PDFGeneratorService;
import com.example.suchishoiliWeb.suchishoili.service.SteadFastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    PDFGeneratorService pdfGeneratorService;

    @Autowired
    EXCELGeneratorService excelGeneratorService;

    @Autowired
    SteadFastService steadFastService;

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @GetMapping("/cancelOrder")
    public ResponseEntity<String> cancelOrder(HttpServletRequest request) {
        Long orderUniqeID = Long.parseLong(request.getParameter("orderUniqeID").trim());
        Order order = orderRepository.findByOrderUniqueID(orderUniqeID);
        order.setDeliveryStatus(DeliveryStatus.ORDER_CANCEL);
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
        for (Order order : orderList) {
            if (order.getId() == orderID) {
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

    @GetMapping("/findOrderListDetails")
    public @ResponseBody
    List<OrderDao> findOrderListDetails(HttpServletRequest request, HttpServletResponse response) {
        String selectFilter = request.getParameter("selectFilter").trim();
        List<Product> productList = productRepository.findAll();
        List<OrderDao> orderDaoList = null;
        List<Order> orderList = null;
        LocalDate now = LocalDate.now();
        if (selectFilter.toLowerCase().equals("today")) {
            LocalDateTime startTime = now.atStartOfDay();
            LocalDateTime endTime = now.atTime(23, 59, 59);
            orderList = orderRepository.findByOrderDateAndtimeBetween(startTime, endTime);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        } else if (selectFilter.toLowerCase().equals("yesterday")) {
            LocalDateTime startTime = now.minusDays(1).atStartOfDay();
            LocalDateTime endTime = now.minusDays(1).atTime(23, 59, 59);
            orderList = orderRepository.findByOrderDateAndtimeBetween(startTime, endTime);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        } else if (selectFilter.toLowerCase().equals("this week")) {
            LocalDateTime firstDayOfWeek = now.minusDays(now.getDayOfWeek().getValue()).atStartOfDay();
            LocalDateTime currentTime = LocalDateTime.now();
            orderList = orderRepository.findByOrderDateAndtimeBetween(firstDayOfWeek,
                    currentTime);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        } else if (selectFilter.toLowerCase().equals("last week")) {
            LocalDateTime lastWeekStartDay =
                    now.minusDays(now.getDayOfWeek().getValue()).atStartOfDay().minusDays(7);
            LocalDateTime lastWeekEndDay =
                    LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()).minusDays(1).atTime(23, 59, 59);
            orderList = orderRepository.findByOrderDateAndtimeBetween(lastWeekStartDay,
                    lastWeekEndDay);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        } else if (selectFilter.toLowerCase().equals("this month")) {
            LocalDateTime startDay = now.minusDays(now.getDayOfMonth()).atStartOfDay();
            LocalDateTime currentTime = LocalDateTime.now();
            System.out.println(startDay);
            orderList = orderRepository.findByOrderDateAndtimeBetween(startDay, currentTime);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        } else if (selectFilter.toLowerCase().equals("last month")) {
            int currentMonthValue = now.getMonthValue();
            LocalDateTime endDay = now.minusDays(now.getDayOfMonth() + 1).atTime(23, 59, 59);
            LocalDateTime startDay = null;
            LocalDate previousMonth = now.minusMonths(1);
            startDay = previousMonth.minusDays(previousMonth.getDayOfMonth() - 1).atStartOfDay();
            orderList = orderRepository.findByOrderDateAndtimeBetween(startDay, endDay);
            orderDaoList = orderService.getOrdersForOrderList(productList, orderList);
        }
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
            SteadFastResponseDAO steadFastResponseDAO = null;
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

    @GetMapping("detailsOrder")
    public ResponseEntity<String> checkDeliveryStatus(HttpServletRequest request) {
        String orderUniqueID = request.getParameter("orderUniqueID").trim();
        Long invoiceID = Long.parseLong(orderUniqueID);
        SteadFastResponseDAO responseDAO = null;
        try {
            responseDAO = steadFastService.checking_delivery_status(orderUniqueID);
//            responseDAO = steadFastService.get_balance();
        } catch (JsonProcessingException e) {
            logger.error("Could not parse the json for SteadFast API checking_delivery_status. " +
                    "(error: {})", e.getMessage());
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (responseDAO.getStatus() != 200) {
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok("1");
    }

    @GetMapping("/invoiceDownload")
    public ResponseEntity<InputStreamResource> invoiceDownload(HttpServletRequest request) {
        String orderFromRequest = request.getParameter("order").trim();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> orderMap = null;
        try {
            orderMap = mapper.readValue(orderFromRequest, Map.class);
        } catch (JsonProcessingException e) {
            logger.error("Could not parse the json for invoice download. " + "(error: {})",
                    e.getMessage());
            return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        OrderDao order = mapper.convertValue(orderMap, OrderDao.class);
        LocalDateTime orderedTime =
                orderRepository.findByOrderUniqueID(order.getOrdeUniqueID()).getOrderDateAndtime();
        order.setOrderedTime(orderedTime);
        File pdf = null;
        try {
            pdf = pdfGeneratorService.PDFExport(order);
        } catch (FileNotFoundException e) {
            logger.error("Could not write the pdf in the output location. (error: {})",
                    e.getMessage());
            return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DocumentException e) {
            logger.error("Could not read the pdf document or add content to the pdf document " +
                    "or set the width of table in the pdf (error: {})", e.getMessage());
            return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (URISyntaxException e) {
            logger.error("Could not parse the path of the logo or watermark image to an uri. " +
                    "(error: {})", e.getMessage());
            return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.error("Could not read the logo or watermark image file. (error: {})",
                    e.getMessage());
            return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders respHeaders = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("application/pdf");
        respHeaders.setContentType(mediaType);
        respHeaders.setContentLength(pdf.length());
        respHeaders.setContentDispositionFormData("attachment", pdf.getName());
        InputStreamResource isr = null;
        try {
            isr = new InputStreamResource(new FileInputStream(pdf));
            return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            logger.error("Could not find the pdf file. (error: {})", e.getMessage());
            return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/excelDownload")
    public ResponseEntity<Resource> excelDownload(HttpServletRequest request) {
        String excelFileName = "";
        String excelDate = request.getParameter("date").trim();
        String ordersFromRequest = request.getParameter("orderList").trim();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> orderListMap = null;
        LocalDate now = LocalDate.now();
        if (excelDate.toLowerCase().equals("today")) {
            excelFileName = now.toString();
        } else if (excelDate.toLowerCase().equals("yesterday")) {
            excelFileName = now.minusDays(1).toString();
        } else if (excelDate.toLowerCase().equals("this week")) {
            LocalDate firstDayOfWeek = now.minusDays(now.getDayOfWeek().getValue());
            excelFileName = firstDayOfWeek.toString() + "TO" + now.toString();
        } else if (excelDate.toLowerCase().equals("last week")) {
            LocalDate lastWeekStartDay = now.minusDays(now.getDayOfWeek().getValue()).minusDays(7);
            LocalDate lastWeekEndDay = now.minusDays(now.getDayOfWeek().getValue()).minusDays(1);
            excelFileName = lastWeekStartDay.toString() + "TO" + lastWeekEndDay.toString();
        } else if (excelDate.toLowerCase().equals("this month")) {
            excelFileName = now.getMonth().toString() + " " + String.valueOf(now.getYear());
        } else if (excelDate.toLowerCase().equals("last month")) {
            LocalDate previousMonth = now.minusMonths(1);
            excelFileName =
                    previousMonth.getMonth().toString() + " " + String.valueOf(previousMonth.getYear());
        }
        try {
            orderListMap = mapper.readValue(ordersFromRequest, new TypeReference<List<Map<String,
                    Object>>>() {
            });
        } catch (JsonProcessingException e) {
            logger.error("Could not parse the json for excel download. " + "(error: {})",
                    e.getMessage());
            return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<OrderDao> orderList = new LinkedList<>();
        for (int i = 0; i < orderListMap.size(); i++) {
            OrderDao order = mapper.convertValue(orderListMap.get(i), OrderDao.class);
            LocalDateTime orderedTime =
                    orderRepository.findByOrderUniqueID(order.getOrdeUniqueID()).getOrderDateAndtime();
            order.setOrderedTime(orderedTime);
            orderList.add(order);
        }
        ByteArrayInputStream stream = null;
        try {
            stream = excelGeneratorService.EXCELExport(orderList);
        } catch (IOException e) {
            logger.error("Could not write the ByteArrayOutputStream to the workbook or " +
                    "close the wordkbook" +
                    "(error: {})", e.getMessage());
            return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        InputStreamResource file = new InputStreamResource(stream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + excelFileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
