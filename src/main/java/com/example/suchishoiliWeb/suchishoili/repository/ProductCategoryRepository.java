package com.example.suchishoiliWeb.suchishoili.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.suchishoiliWeb.suchishoili.model.ProductCategory;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>{
//	@Query("SELECT c FROM products_category c WHERE c.category = ?1")
//	List<ProductCategory> find
	ProductCategory findByCategory(String category);
	
//	Long deleteBySubCategory(String subCategory);
//	@Query("SELECT pc.category FROM products_category AS pc GROUP BY c.category")
//	List<CategoryGroup> allCategoryByCategoryName();
//	@Modifying
//	@Query("delete from products_category pc where pc.sub_category=:subCategory")
//	void deleteSubCategory(@Param("title") String subCategory);
}
