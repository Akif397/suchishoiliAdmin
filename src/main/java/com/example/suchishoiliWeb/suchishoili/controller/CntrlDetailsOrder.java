package com.example.suchishoiliWeb.suchishoili.controller;

import com.example.suchishoiliWeb.suchishoili.DAO.SteadFastResponseDao;
import com.example.suchishoiliWeb.suchishoili.service.SteadFastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CntrlDetailsOrder {
    private Logger logger = LoggerFactory.getLogger(CntrlDetailsOrder.class);
    private final SteadFastService steadFastService;

    @Autowired
    public CntrlDetailsOrder(SteadFastService steadFastService) {
        this.steadFastService = steadFastService;
    }

    @GetMapping("detailsOrder")
    public ResponseEntity<String> checkDeliveryStatus(HttpServletRequest request) {
        String orderUniqueID = request.getParameter("orderUniqueID").trim();
        Long invoiceID = Long.parseLong(orderUniqueID);
        SteadFastResponseDao responseDAO = null;
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
}
