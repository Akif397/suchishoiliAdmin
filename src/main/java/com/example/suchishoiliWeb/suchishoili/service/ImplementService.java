package com.example.suchishoiliWeb.suchishoili.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.suchishoiliWeb.suchishoili.model.Product;
import com.example.suchishoiliWeb.suchishoili.model.ProductCategory;
import com.example.suchishoiliWeb.suchishoili.repository.ProductCategoryRepository;
import com.example.suchishoiliWeb.suchishoili.repository.ProductRepository;

@Service
public class ImplementService implements ProductService, ProductCategoryService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	
	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
	@Override
	public void addProduct(Product product) {
		this.productRepository.save(product);		
	}
	@Override
	public List<ProductCategory> getAllProductCategories() {
		return productCategoryRepository.findAll();
	}
	@Override
	public Optional<ProductCategory> findCategory(long id) {
		return productCategoryRepository.findById(id);
	}
	@Override
	public Optional<ProductCategory> findCategoryByCategory(String category) {
//		@Query("SELECT c FROM products_category c WHERE c.category = ")
		return null;
	}
}
