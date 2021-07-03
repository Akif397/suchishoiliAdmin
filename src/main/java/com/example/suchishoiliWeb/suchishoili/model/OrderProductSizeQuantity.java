package com.example.suchishoiliWeb.suchishoili.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_product_size_quantity")
public class OrderProductSizeQuantity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 32)
	private Long orderId;
	
	@Column(nullable = false, length = 32)
	private Long productSizeId;
	
	@Column(nullable = false, length = 32)
	private String productSize;
	
	@Column(nullable = false, length = 32)
	private int orderQuantity;
	
	@Column(nullable = false, length = 32)
	private int discount;
	
	
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long order_id) {
		this.orderId = order_id;
	}

	public Long getProductSizeId() {
		return productSizeId;
	}

	public void setProductSizeId(Long product_id) {
		this.productSizeId = product_id;
	}

	public String getProductSize() {
		return productSize;
	}

	public void setProductSize(String product_size) {
		this.productSize = product_size;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
