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
@Table(name = "products_subcategory")
public class ProductSubcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 20)
    private String subCategory;

    @OneToMany(targetEntity = SubcategorySize.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "sc_fk", referencedColumnName = "id")
    private List<SubcategorySize> sizesAndQuantities;

    public List<SubcategorySize> getProductSizesAndQuantities() {
        return sizesAndQuantities;
    }

    public void setProductSizesAndQuantities(List<SubcategorySize> sizesAndQuantities) {
        this.sizesAndQuantities = sizesAndQuantities;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
}
