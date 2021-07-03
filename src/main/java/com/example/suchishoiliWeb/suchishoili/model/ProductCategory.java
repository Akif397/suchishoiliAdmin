package com.example.suchishoiliWeb.suchishoili.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="products_category")
public class ProductCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false, length = 20)
	private String category;
	
	@OneToMany(targetEntity = ProductSubcategory.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_fk", referencedColumnName = "id")
	private List<ProductSubcategory> subCategories;
	
	public List<ProductSubcategory> getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(List<ProductSubcategory> subCategories) {
		this.subCategories = subCategories;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
