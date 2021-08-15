package com.example.suchishoiliWeb.suchishoili.repository;

import java.sql.ResultSet;
import java.util.List;

import com.example.suchishoiliWeb.suchishoili.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.suchishoiliWeb.suchishoili.model.Product;
import com.example.suchishoiliWeb.suchishoili.model.ProductSubcategory;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductSubcategory(ProductSubcategory subcategory);

    //    List<Product> findByProductSubcategoryAndIs_image_added(ProductSubcategory subcategory,
//                                                               boolean is_image_added);
    @Query(value = "SELECT * FROM products p WHERE p.subcategory_id = ?1 and p.is_image_added = ?2", nativeQuery = true)
    List<Product> findByProductSubcategoryAndIs_image_added(ProductSubcategory ps, boolean is_image_added);

    Product findByNameAndProductSubcategory(String name, ProductSubcategory productSubcategory);

    Product findByIdAndName(Long id, String name);

    @Query(value = "SELECT * FROM products p WHERE p.id = ?1", nativeQuery = true)
    Product findByProductId(Long productId);
}
