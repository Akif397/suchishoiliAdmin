package com.example.suchishoiliWeb.suchishoili.repository;

import com.example.suchishoiliWeb.suchishoili.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.suchishoiliWeb.suchishoili.model.SubcategorySize;

import java.util.List;

@Repository
public interface SubcategorySizeRepository extends JpaRepository<SubcategorySize, Long>{
	SubcategorySize findBySize(String size);

	@Query(value = "SELECT ps.* FROM product_size ps WHERE ps.size = ?1 AND ps.sc_fk = ?2",
			nativeQuery =	true)
	SubcategorySize findBySizeAndSubcategoryId(String size, Long subcategoryId);

//	SubcategorySize findByIdAndOrderById(Long productSizeId, Long orderID);

	@Query(value = "SELECT pso.subcategory_size_id FROM product_size_orders pso WHERE pso.orders_id = ?1", nativeQuery =
			true)
	List<Long> findByOrders(Long orderID);

	@Query(value = "SELECT * FROM products p WHERE p.id = ?1", nativeQuery = true)
	Product findByProductId(Long productId);
}
