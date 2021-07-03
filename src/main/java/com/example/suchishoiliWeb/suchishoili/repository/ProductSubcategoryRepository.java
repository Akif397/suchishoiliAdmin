package com.example.suchishoiliWeb.suchishoili.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.suchishoiliWeb.suchishoili.model.ProductSubcategory;

@Repository
public interface ProductSubcategoryRepository extends JpaRepository<ProductSubcategory, Long> {
//	List<ProductSubcategory> findByProductCategory(ProductCategory productCategory);
	void deleteBySubCategory(String subCategory);
	ProductSubcategory findBySubCategory(String subCategory);
}
