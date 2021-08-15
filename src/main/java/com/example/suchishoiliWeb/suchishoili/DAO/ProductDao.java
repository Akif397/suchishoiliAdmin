package com.example.suchishoiliWeb.suchishoili.DAO;

import java.util.List;

public class ProductDao {
    private Long id;
    private Long productSizeID;
    private String name;
    private List<String> sizes;
    //this is just for watchInventory
    private List<SubcategorySizeDao> subcategorySizeDaoList;
    private int prize;
    private List<Integer> quantities;
    private int orderDiscount;

    public List<SubcategorySizeDao> getSubcategorySizeDaoList() {
        return subcategorySizeDaoList;
    }

    public void setSubcategorySizeDaoList(List<SubcategorySizeDao> subcategorySizeDaoList) {
        this.subcategorySizeDaoList = subcategorySizeDaoList;
    }

    public Long getProductSizeID() {
        return productSizeID;
    }

    public void setProductSizeID(Long productSizeID) {
        this.productSizeID = productSizeID;
    }

    public int getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(int orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Integer> quantities) {
        this.quantities = quantities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }


}
