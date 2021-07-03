package com.example.suchishoiliWeb.suchishoili.model;

import java.util.List;

public class CategoryGroup {
	private int id;
	private String categoryName;
	private List<String> subCategoryList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<String> getSubCategoryList() {
		return subCategoryList;
	}
	public void setSubCategoryList(List<String> subCategoryList) {
		this.subCategoryList = subCategoryList;
	}
	
}
