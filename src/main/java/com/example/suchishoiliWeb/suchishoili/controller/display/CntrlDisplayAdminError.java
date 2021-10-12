package com.example.suchishoiliWeb.suchishoili.controller.display;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.ResponseEntity.ok;

@Controller
@ComponentScan(basePackages = {"com.example.suchishoiliWeb.suchishoili.service"})
public class CntrlDisplayAdminError implements ErrorController {
    private Logger logger = LoggerFactory.getLogger(CntrlDisplayAdminError.class);

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//
//            if (statusCode == HttpStatus.NOT_FOUND.value()) {
//                return "error-404";
//            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                return "error-500";
//            }
//        }
        return "admin/error";
    }
}

