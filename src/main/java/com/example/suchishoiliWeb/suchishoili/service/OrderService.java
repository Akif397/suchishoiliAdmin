package com.example.suchishoiliWeb.suchishoili.service;

import com.example.suchishoiliWeb.suchishoili.DAO.OrderDao;
import com.example.suchishoiliWeb.suchishoili.DAO.ProductDao;
import com.example.suchishoiliWeb.suchishoili.DAO.UserDao;
import com.example.suchishoiliWeb.suchishoili.SuchishoiliApplication;
import com.example.suchishoiliWeb.suchishoili.model.Order;
import com.example.suchishoiliWeb.suchishoili.model.OrderProductSizeQuantity;
import com.example.suchishoiliWeb.suchishoili.model.Product;
import com.example.suchishoiliWeb.suchishoili.model.SubcategorySize;
import com.example.suchishoiliWeb.suchishoili.repository.OrderProductSizeQuantityRepository;
import com.example.suchishoiliWeb.suchishoili.repository.OrderRepository;
import com.example.suchishoiliWeb.suchishoili.repository.SubcategorySizeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    SubcategorySizeRepository subcategorySizeRepository;
    @Autowired
    OrderProductSizeQuantityRepository orderProductSizeQuantityRepository;
    @Autowired
    OrderRepository orderRepository;

    private Logger logger = LoggerFactory.getLogger(SuchishoiliApplication.class);

    public List<OrderDao> getOrdersForOrderList(List<Product> productList, List<Order> orderList) {
        List<OrderDao> orderDaoList = new LinkedList<>();
        for (Order orderFromDB : orderList) {
            UserDao userDao = new UserDao();
            OrderDao orderDao = new OrderDao();
            orderDao.setOrderID(orderFromDB.getId());
            orderDao.setOrdeUniqueID(orderFromDB.getOrderUniqueID());
            orderDao.setDeliveryStatus(orderFromDB.getDeliveryStatus());
            orderDao.setOrderDiscount(orderFromDB.getOrderDiscount());
            orderDao.setOrderFrom(orderFromDB.getOrderFrom());
            orderDao.setPaymentStatus(orderFromDB.isPaymentStatus() ? "Paid" : "Unpaid");
            orderDao.setOrderNote(orderFromDB.getNote());
            orderDao.setPaymentMethod(orderFromDB.getPaymentMethod());
            userDao.setName(orderFromDB.getUser().getName());
            userDao.setLocation(orderFromDB.getUser().getAddressLocation());
            userDao.setPhone(orderFromDB.getUser().getPhoneNumber());
            userDao.setAddress(orderFromDB.getUser().getAddress());
            orderDao.setUserDao(userDao);
            List<ProductDao> producDaotList = new LinkedList<>();
            List<Long> longs = subcategorySizeRepository.findByOrders(orderFromDB.getId());
            for (Product product : productList) {
                List<SubcategorySize> subcategorySizeList = product.getSizes();
                for (SubcategorySize size : subcategorySizeList) {
                    for (int i = 0; i < longs.size(); i++) {
                        if (longs.get(i) == size.getId()) {
                            OrderProductSizeQuantity orderProductSizeQuantityListFromDB =
                                    orderProductSizeQuantityRepository.findByOrderIdAndProductSizeId(orderFromDB.getId(),
                                            size.getId());
                            ProductDao productDao = new ProductDao();
                            productDao.setName(product.getName());
                            productDao.setPrize(product.getPrize());
                            productDao.setId(product.getId());
                            productDao.setOrderDiscount(orderProductSizeQuantityListFromDB.getDiscount());
                            List<String> productOrderedSizes = new LinkedList<>();
                            List<Integer> productOrderedQuantities = new LinkedList<>();
                            productOrderedSizes.add(orderProductSizeQuantityListFromDB.getProductSize());
                            productOrderedQuantities.add(orderProductSizeQuantityListFromDB.getOrderQuantity());
                            productDao.setQuantities(productOrderedQuantities);
                            productDao.setSizes(productOrderedSizes);
                            productDao.setProductSizeID(orderProductSizeQuantityListFromDB.getProductSizeId());
                            producDaotList.add(productDao);
                        }
                    }
                }
            }

            orderDao.setProductDaos(producDaotList);
            orderDaoList.add(orderDao);
        }
        return orderDaoList;
    }

}
