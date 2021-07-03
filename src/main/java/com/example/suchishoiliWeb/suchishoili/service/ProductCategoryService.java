package com.example.suchishoiliWeb.suchishoili.service;

import java.util.List;
import java.util.Optional;

import com.example.suchishoiliWeb.suchishoili.model.ProductCategory;

public interface ProductCategoryService {
	List<ProductCategory> getAllProductCategories();
	Optional<ProductCategory> findCategory(long id);
	Optional<ProductCategory> findCategoryByCategory(String category);
}
