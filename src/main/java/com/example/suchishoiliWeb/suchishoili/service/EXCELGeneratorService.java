package com.example.suchishoiliWeb.suchishoili.service;

import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.DAO.ProductDao;
import com.example.suchishoiliWeb.suchishoili.SuchishoiliApplication;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class EXCELGeneratorService {
    private Logger logger = LoggerFactory.getLogger(SuchishoiliApplication.class);

    @Deprecated
    private String getEXCELFileLocation(String excelFileName) {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "excel" + File.separator +
                excelFileName + ".xlsx";
        return fileLocation;
    }

    private void createHeaderForEXCEL(Workbook workbook, Sheet sheet) {
        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setWrapText(true);

        XSSFFont headerFont = ((XSSFWorkbook) workbook).createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);
        headerFont.setColor(new XSSFColor(new Color(255, 255, 255)));
        headerStyle.setFont(headerFont);

        Cell headerCell = header.createCell(0);

        headerCell.setCellValue("#");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Customer Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Phone Number");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Total Price");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Products");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(5);
        headerCell.setCellValue("Delivery Status");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(6);
        headerCell.setCellValue("Payment Status");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(7);
        headerCell.setCellValue("Payment Method");
        headerCell.setCellStyle(headerStyle);
    }

    private void createColumnsForEXCEL(Sheet sheet) {
        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 10000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
    }

    private void writeRowsForEXCEL(Workbook workbook, Sheet sheet, List<OrderDao> orderDaoList) {
        int rowNumber = 1;
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);

        for (OrderDao orderDao : orderDaoList) {
            String customerName = orderDao.getUserDao().getName();
            String customerPhoneNumber = orderDao.getUserDao().getPhone();
            String deliveryStatus = orderDao.getDeliveryStatus();
            String paymentStatus = orderDao.getPaymentStatus();
            String paymentMethod = orderDao.getPaymentMethod();
            String products = "";
            int totatPrice = 0;
            int orderDiscount = orderDao.getOrderDiscount();

            List<ProductDao> productDaoList = orderDao.getProductDaos();
            for (ProductDao productDao : productDaoList) {
                products = products + productDao.getName() + " -> ";
                for (int i = 0; i < productDao.getSizes().size(); i++) {
                    totatPrice = totatPrice + productDao.getPrize() - productDao.getOrderDiscount();
                    if (i != productDao.getSizes().size() - 1) {
                        products = products + productDao.getSizes().get(i) + ", ";
                    } else {
                        products = products + productDao.getSizes().get(i) + "; ";
                    }

                }
            }
            totatPrice = totatPrice - orderDiscount;
            Row row = sheet.createRow(rowNumber);

            Cell cell = row.createCell(0);
            cell.setCellValue(rowNumber);
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue(customerName);
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue(customerPhoneNumber);
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue(totatPrice);
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue(products);
            cell.setCellStyle(cellStyle);

            cell = row.createCell(5);
            cell.setCellValue(deliveryStatus);
            cell.setCellStyle(cellStyle);

            cell = row.createCell(6);
            cell.setCellValue(paymentStatus);
            cell.setCellStyle(cellStyle);

            cell = row.createCell(7);
            cell.setCellValue(paymentMethod);
            cell.setCellStyle(cellStyle);

            rowNumber++;
        }
    }

    public ByteArrayInputStream EXCELExport(List<OrderDao> orderDaoList) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Orders");
        this.createColumnsForEXCEL(sheet);
        this.createHeaderForEXCEL(workbook, sheet);
        this.writeRowsForEXCEL(workbook, sheet, orderDaoList);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return new ByteArrayInputStream(stream.toByteArray());
    }
}
