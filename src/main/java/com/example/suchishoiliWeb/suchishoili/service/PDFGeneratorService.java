package com.example.suchishoiliWeb.suchishoili.service;

import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.DAO.ProductDao;
import com.example.suchishoiliWeb.suchishoili.DAO.UserDao;
import com.example.suchishoiliWeb.suchishoili.SuchishoiliApplication;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryCharge;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PDFGeneratorService {
    @Autowired
    OrderService orderService;

    private Logger logger = LoggerFactory.getLogger(SuchishoiliApplication.class);

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(BaseColor.WHITE);
        font.setSize(8);

        cell.setPhrase(new Phrase("#", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Product Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Size", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Price", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Discount", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Total Price", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, OrderDao order) {
        int count = 1;
        int totalPrice = 0;
        int orderDiscount = order.getOrderDiscount();

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(BaseColor.BLACK.darker());
        font.setSize(8);

        List<ProductDao> productDaoList = order.getProductDaos();

        for (ProductDao productDao : productDaoList) {
            PdfPCell cell = new PdfPCell();
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            if (productDao.getSizes().size() > 1) {
                for (int i = 0; i < productDao.getSizes().size(); i++) {
                    cell.setPhrase(new Phrase(String.valueOf(count), font));
                    table.addCell(cell);

                    cell.setPhrase(new Phrase(productDao.getName(), font));
                    table.addCell(cell);

                    cell.setPhrase(new Phrase(String.valueOf(productDao.getSizes().get(i)),
                            font));
                    table.addCell(cell);

                    cell.setPhrase(new Phrase(String.valueOf(productDao.getQuantities().get(i)),
                            font));
                    table.addCell(cell);

                    cell.setPhrase(new Phrase(String.valueOf(productDao.getPrize()) + "/-", font));
                    table.addCell(cell);

                    cell.setPhrase(new Phrase(String.valueOf(productDao.getOrderDiscount()) + "/-", font));
                    table.addCell(cell);

                    cell.setPhrase(new Phrase(String.valueOf((productDao.getPrize() *
                            productDao.getQuantities().get(i)) - productDao.getOrderDiscount()) + "/-", font));
                    table.addCell(cell);
                }
            } else {
                cell.setPhrase(new Phrase(String.valueOf(count), font));
                table.addCell(cell);

                cell.setPhrase(new Phrase(productDao.getName(), font));
                table.addCell(cell);

                cell.setPhrase(new Phrase(String.valueOf(productDao.getSizes().get(0)), font));
                table.addCell(cell);

                cell.setPhrase(new Phrase(String.valueOf(productDao.getQuantities().get(0)), font));
                table.addCell(cell);

                cell.setPhrase(new Phrase(String.valueOf(productDao.getPrize()) + "/-", font));
                table.addCell(cell);

                cell.setPhrase(new Phrase(String.valueOf(productDao.getOrderDiscount()) +
                        "/-", font));
                table.addCell(cell);

                cell.setPhrase(new Phrase(String.valueOf((productDao.getPrize() *
                        productDao.getQuantities().get(0)) - productDao.getOrderDiscount()) +
                        "/-", font));
                table.addCell(cell);
                totalPrice += (productDao.getPrize() * productDao.getQuantities().get(0)) - productDao.getOrderDiscount();
                count++;
            }
        }

        PdfPCell extraCell = new PdfPCell();
        extraCell.setColspan(6);
        extraCell.setBorder(0);
        extraCell.setPaddingTop(10f);
        extraCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell extraCell1 = new PdfPCell();
        extraCell1.setColspan(6);
        extraCell1.setBorder(0);
        extraCell1.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell totalCell = new PdfPCell();
        totalCell.setBorder(0);
        totalCell.setPaddingTop(10f);
        totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell totalCell1 = new PdfPCell();
        totalCell1.setBorder(0);
        totalCell1.setHorizontalAlignment(Element.ALIGN_CENTER);

        extraCell.setPhrase(new Phrase("Total:", font));
        table.addCell(extraCell);

        totalCell.setPhrase(new Phrase(String.valueOf(totalPrice) + "/-", font));
        table.addCell(totalCell);

        extraCell1.setPhrase(new Phrase("Discount(On full order):", font));
        table.addCell(extraCell1);

        totalCell1.setPhrase(new Phrase(String.valueOf(orderDiscount) + "/-", font));
        table.addCell(totalCell1);

        extraCell1.setPhrase(new Phrase("Delivery Charge:", font));
        table.addCell(extraCell1);

        if (order.getUserDao().getLocation().trim().equals("Inside Dhaka")) {
            totalCell1.setPhrase(new Phrase(String.valueOf(DeliveryCharge.INSIDE_DHAKA) + "/-",
                    font));
            totalPrice = totalPrice + DeliveryCharge.INSIDE_DHAKA;
        } else {
            totalCell1.setPhrase(new Phrase(String.valueOf(DeliveryCharge.OUTSIDE_DHAKA) + "/-",
                    font));
            totalPrice = totalPrice + DeliveryCharge.OUTSIDE_DHAKA;
        }
        table.addCell(totalCell1);

        extraCell1.setPhrase(new Phrase("Amount to pay:", font));
        table.addCell(extraCell1);

        int amountToPay = totalPrice - orderDiscount;
        if (order.getPaymentStatus().trim().equals("Paid")) {
            totalCell1.setPhrase(new Phrase(String.valueOf(amountToPay) + "/- (" + order.getPaymentStatus() + ")",
                    font));
        } else {
            totalCell1.setPhrase(new Phrase(String.valueOf(amountToPay) + "/-", font));
        }
        table.addCell(totalCell1);
    }

    private PdfPTable createTable() throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{.5f, 5f, 1f, 1.0f, 1f, 1f, 2f});
        table.getDefaultCell().setBorder(0);
        table.setSpacingBefore(150);
        return table;
    }

    private void customerDetails(Document document, UserDao user, LocalDateTime orderTime) throws DocumentException {
        String name = user.getName();
        String address = user.getAddress();
        String phoneNumber = user.getPhone();
        String time =
                String.valueOf(orderTime.getHour()) + ":" + String.valueOf(orderTime.getMinute());
        String date =
                String.valueOf(orderTime.getDayOfMonth()) + " " + orderTime.getMonth().toString() +
                        ", " + String.valueOf(orderTime.getYear());

        Font otherFont = FontFactory.getFont(FontFactory.TIMES_ITALIC);
        otherFont.setSize(10);

        Font detailsFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        detailsFont.setSize(12);

        Paragraph orderTimeParagraph = new Paragraph("Order Time:", otherFont);
        orderTimeParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(orderTimeParagraph);
        Paragraph timeParagraph = new Paragraph(time, detailsFont);
        timeParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(timeParagraph);

        Paragraph dateParagraph = new Paragraph(date, detailsFont);
        dateParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(dateParagraph);

        Paragraph otherParagraph = new Paragraph("Customer Details:", otherFont);
        otherParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(otherParagraph);

        Paragraph nameParagraph = new Paragraph(name, detailsFont);
        nameParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(nameParagraph);

        Paragraph addressParagraph = new Paragraph(address, detailsFont);
        addressParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(addressParagraph);

        Paragraph aphoneNoParagraph = new Paragraph(phoneNumber, detailsFont);
        aphoneNoParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(aphoneNoParagraph);
    }

    private void suchishoiliLogo(Document document) throws URISyntaxException, DocumentException, IOException {
        Path pathForLogo = null;
        Image logo = null;
        pathForLogo =
                Paths.get(getClass().getClassLoader().getResource("static/images/logo_invoice.png").toURI());

//        try {
        logo = Image.getInstance(pathForLogo.toAbsolutePath().toString());
//        } catch (BadElementException e) {
//            logger.error("Could not get the instance of the logo image. (error: {})",
//                    e.getMessage());
//        }
        logo.setAbsolutePosition(30f, 720f);
        document.add(logo);
    }

    private void barcode(Document document) throws DocumentException {
        BarcodeQRCode qrCode = new BarcodeQRCode("www.suchishoili.com", 1, 1, null);
        Image qrCOdeImage = qrCode.getImage();
        qrCOdeImage.setAbsolutePosition(275f, 750f);
        document.add(qrCOdeImage);
    }

    public File PDFExport(OrderDao order) throws IOException, DocumentException, URISyntaxException {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = null;
        PdfContentByte cb = null;

        String orderUniqueId = order.getOrdeUniqueID().toString();
        UserDao customer = order.getUserDao();

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation =
                path.substring(0, path.length() - 1) + "invoice" + File.separator + orderUniqueId +
                        ".pdf";

        writer = PdfWriter.getInstance(document, new FileOutputStream(fileLocation));
        document.open();
        suchishoiliLogo(document);
        barcode(document);
        customerDetails(document, customer, order.getOrderedTime());
        PdfPTable table = createTable();
        writeTableHeader(table);
        writeTableData(table, order);

        document.add(table);

        Font detailsFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        detailsFont.setSize(12);
        if (order.getOrderNote() != null) {
            Paragraph noteParagraph = new Paragraph(order.getOrderNote(),
                    detailsFont);
            cb = writer.getDirectContent();
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, noteParagraph, 10, 10, 0);
        }
        document.close();
        File pdf = new File(fileLocation);
        return pdf;
    }
}
