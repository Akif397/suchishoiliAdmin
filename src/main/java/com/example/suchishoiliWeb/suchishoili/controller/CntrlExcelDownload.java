package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.repository.OrderRepository;
import com.example.suchishoiliWeb.suchishoili.service.EXCELGeneratorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class CntrlExcelDownload {
    private Logger logger = LoggerFactory.getLogger(CntrlExcelDownload.class);
    private final OrderRepository orderRepository;
    private final EXCELGeneratorService excelGeneratorService;

    @Autowired
    public CntrlExcelDownload(OrderRepository orderRepository, EXCELGeneratorService excelGeneratorService) {
        this.orderRepository = orderRepository;
        this.excelGeneratorService = excelGeneratorService;
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
