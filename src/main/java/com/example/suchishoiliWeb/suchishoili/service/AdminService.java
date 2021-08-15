package com.example.suchishoiliWeb.suchishoili.service;

import com.example.suchishoiliWeb.suchishoili.DAO.DashboardDao;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.AdminFixedValue;
import com.example.suchishoiliWeb.suchishoili.fixedVariables.DeliveryStatus;
import com.example.suchishoiliWeb.suchishoili.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AdminService {
    public DashboardDao displayAdminDashboard(List<Order> orderList) {
        int deliveredOrderCount = 0;
        int canceledOrderCount = 0;
        int courierOrderCount = 0;
        int factoryOrderCount = 0;
        int confirmedOrderCount = 0;
        for (Order order : orderList) {
            if (order.getDeliveryStatus().trim().equals(DeliveryStatus.DELIVERED)) {
                deliveredOrderCount++;
            }
            if (order.getDeliveryStatus().trim().equals(DeliveryStatus.ORDER_CANCEL)) {
                canceledOrderCount++;
            }
            if (order.getDeliveryStatus().trim().equals(DeliveryStatus.IN_COURIER)) {
                courierOrderCount++;
            }
            if (order.getDeliveryStatus().trim().equals(DeliveryStatus.IN_THE_FACTORY)) {
                factoryOrderCount++;
            }
            if (order.getDeliveryStatus().trim().equals(DeliveryStatus.ORDER_TAKEN)) {
                confirmedOrderCount++;
            }
        }
        DashboardDao dashboardDao = new DashboardDao();
        dashboardDao.setTotalOrder(orderList.size());
        dashboardDao.setConfirmedOrder(confirmedOrderCount);
        dashboardDao.setDeliveredOrder(deliveredOrderCount);
        dashboardDao.setCanceledOrder(canceledOrderCount);
        dashboardDao.setInCourierOrder(courierOrderCount);
        return dashboardDao;
    }

    public void saveProductImage(String categoy, String subCategoy, String productName, MultipartFile[] listImage,
                                 MultipartFile[] detailsImage1, MultipartFile[] detailsImage2,
                                 MultipartFile[] detailsImage3, MultipartFile[] detailsImage4) throws IOException {
        File currDir = new File(System.getProperty("user.dir")).getParentFile();

        File productImageSaveDir = new File(currDir + File.separator + AdminFixedValue.PRODUCT_IMAGE_SAVE_DIR);
        if (!productImageSaveDir.exists()){
            productImageSaveDir.mkdirs();
        }

        File categoryImageSaveDir = new File(productImageSaveDir.getAbsolutePath() + File.separator + categoy);
        if (!categoryImageSaveDir.exists()){
            categoryImageSaveDir.mkdirs();
        }

        File subCategoryImageSaveDir = new File(categoryImageSaveDir + File.separator + subCategoy);
        if (!subCategoryImageSaveDir.exists()){
            subCategoryImageSaveDir.mkdirs();
        }

        File productNameImageSaveDir = new File(subCategoryImageSaveDir + File.separator + productName);
        if (!productNameImageSaveDir.exists()){
            productNameImageSaveDir.mkdirs();
        }

        File listImageSaveDir =
                new File(productNameImageSaveDir + File.separator + AdminFixedValue.LIST_VIEW_FOLDER_NAME);
        if (!listImageSaveDir.exists()){
            listImageSaveDir.mkdirs();
        }
        Path listImageSavePath = Paths.get(listImageSaveDir.getAbsolutePath(),
                productName.replace(" ", "_") + ".jpg");
        Files.write(listImageSavePath, listImage[0].getBytes());

        File detailsImageSaveDir =
                new File(productNameImageSaveDir + File.separator + AdminFixedValue.DETAILS_VIEW_FOLDER_NAME);
        if (!detailsImageSaveDir.exists()){
            detailsImageSaveDir.mkdirs();
        }
        Path detailsImage1SavePath = Paths.get(detailsImageSaveDir.getAbsolutePath(),
                productName.replace(" ", "_") + "1.jpg");
        Files.write(detailsImage1SavePath, detailsImage1[0].getBytes());

        Path detailsImage2SavePath = Paths.get(detailsImageSaveDir.getAbsolutePath(),
                productName.replace(" ", "_") + "2.jpg");
        Files.write(detailsImage2SavePath, detailsImage2[0].getBytes());

        Path detailsImage3SavePath = Paths.get(detailsImageSaveDir.getAbsolutePath(),
                productName.replace(" ", "_") + "3.jpg");
        Files.write(detailsImage3SavePath, detailsImage3[0].getBytes());

        Path detailsImage4SavePath = Paths.get(detailsImageSaveDir.getAbsolutePath(),
                productName.replace(" ", "_") + "4.jpg");
        Files.write(detailsImage4SavePath, detailsImage4[0].getBytes());
//        String path = productImageSaveDir.getAbsolutePath();
//        return path;
    }
}
