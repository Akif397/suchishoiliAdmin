package com.example.suchishoiliWeb.suchishoili.service;

import com.example.suchishoiliWeb.suchishoili.DAO.SteadFastOrderDao;
import com.example.suchishoiliWeb.suchishoili.DAO.SteadFastConsignmentDao;
import com.example.suchishoiliWeb.suchishoili.DAO.SteadFastResponseDao;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.SteadFastAuth;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class SteadFastService {
    private HttpHeaders httpHeaders() {
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.set("Api-Key", SteadFastAuth.API_KEY);
        httpHeader.set("Secret-Key", SteadFastAuth.SECRET_KEY);
        httpHeader.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        httpHeader.setContentType(MediaType.APPLICATION_JSON);
        return httpHeader;
    }

    public SteadFastResponseDao order_create(String orderUniqueID, String recipient_name, String recipient_phone,
                                             String recipient_address, int cod_amount, String note) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = this.httpHeaders();

        SteadFastOrderDao steadFast = new SteadFastOrderDao();
        steadFast.setInvoice(orderUniqueID);
        steadFast.setRecipient_name(recipient_name);
        steadFast.setRecipient_phone(recipient_phone);
        steadFast.setRecipient_address(recipient_address);
        steadFast.setCod_amount(cod_amount);
        steadFast.setNote(note);

        ObjectMapper mapper = new ObjectMapper();
        String jsonValueForSteadFastAPI = null;
        jsonValueForSteadFastAPI = mapper.writeValueAsString(steadFast);
        HttpEntity<String> requestForsteadFast = new HttpEntity<>(jsonValueForSteadFastAPI,
                header);
        ResponseEntity<String> responseFromSteadfast =
                restTemplate.postForEntity(SteadFastAuth.CREATE_ORDER_URL, requestForsteadFast,
                        String.class);
        Map<String, String> steadFastMap =
                mapper.readValue(responseFromSteadfast.getBody(), Map.class);
        SteadFastResponseDao steadFastResponseDAO = new SteadFastResponseDao();
        SteadFastConsignmentDao consignmentDao = mapper.convertValue(steadFastMap.get(
                "consignment"), SteadFastConsignmentDao.class);
        steadFastResponseDAO.setStatus(Integer.parseInt(String.valueOf(steadFastMap.get("status"))));
        steadFastResponseDAO.setMessage(steadFastMap.get("message"));
        steadFastResponseDAO.setConsignment(consignmentDao);
        return steadFastResponseDAO;
    }

    //this invoiceID is the orderUniqueID of each each order
    public SteadFastResponseDao checking_delivery_status(String invoiceID) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = this.httpHeaders();
        HttpEntity<String> requestForsteadFast = new HttpEntity<>(header);
        String url = SteadFastAuth.CHECKING_DELIVERY_STATUS + invoiceID;
        ResponseEntity<String> responseFromSteadfast =
                restTemplate.exchange(SteadFastAuth.CHECKING_DELIVERY_STATUS + invoiceID,
                        HttpMethod.GET, requestForsteadFast, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> steadFastMap =
                mapper.readValue(responseFromSteadfast.getBody(), Map.class);
        SteadFastResponseDao responseDAO = mapper.convertValue(steadFastMap,
                SteadFastResponseDao.class);
        return responseDAO;
    }

    public SteadFastResponseDao get_balance() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = this.httpHeaders();
        HttpEntity<String> requestForsteadFast = new HttpEntity<>(header);
        ResponseEntity<String> responseFromSteadfast =
                restTemplate.exchange(SteadFastAuth.GET_BALANCE, HttpMethod.GET, requestForsteadFast,
                        String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> steadFastMap =
                mapper.readValue(responseFromSteadfast.getBody(), Map.class);
        SteadFastResponseDao responseDAO = mapper.convertValue(steadFastMap,
                SteadFastResponseDao.class);
        return responseDAO;
    }
}
