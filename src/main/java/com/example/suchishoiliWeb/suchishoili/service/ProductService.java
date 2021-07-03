package com.example.suchishoiliWeb.suchishoili.service;

import java.util.List;

import com.example.suchishoiliWeb.suchishoili.model.Product;

public interface ProductService {
	List<Product> getAllProducts();
	void addProduct(Product product);
}
