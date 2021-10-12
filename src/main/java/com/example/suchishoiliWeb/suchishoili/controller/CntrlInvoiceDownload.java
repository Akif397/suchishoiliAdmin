package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.repository.OrderRepository;
import com.example.suchishoiliWeb.suchishoili.service.PDFGeneratorService;
import com.example.suchishoiliWeb.suchishoili.service.SteadFastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Map;

public class CntrlInvoiceDownload {
    private Logger logger = LoggerFactory.getLogger(CntrlInvoiceDownload.class);
    private final OrderRepository orderRepository;
    private final PDFGeneratorService pdfGeneratorService;

    @Autowired
    public CntrlInvoiceDownload(OrderRepository orderRepository,
                                PDFGeneratorService pdfGeneratorService) {
        this.orderRepository = orderRepository;
        this.pdfGeneratorService = pdfGeneratorService;
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
}
