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

//	SubcategorySize findByIdAndOrderById(Long productSizeId, Long orderID);

	@Query(value = "SELECT pso.subcategory_size_id FROM product_size_orders pso WHERE pso.orders_id = ?1", nativeQuery =
			true)
	List<Long> findByOrders(Long orderID);

	@Query(value = "SELECT * FROM products p WHERE p.id = ?1", nativeQuery = true)
	Product findByProductSizeId(Long productId);
}
