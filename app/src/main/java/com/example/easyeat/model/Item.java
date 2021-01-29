package com.example.easyeat.model;

import com.example.easyeat.Api.Holders.ItemHolder;

import java.io.Serializable;


public class Item implements Serializable
{
    public int id;
    public int categoryId;
    public int subCategoryId;
    public String name;
    public double unitPrice;
    public int url;

    public Item(int id, int categoryId, int subCategoryId, String name, double unitPrice,int url)
    {
        this.id = id;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.url = url;
    }

    public Item(ItemHolder itemHolder, int url)
    {
        this.id = itemHolder.getId();
        this.categoryId = itemHolder.getCategoryId();
        this.subCategoryId = itemHolder.getSubCategoryId();
        this.name = itemHolder.getName();
        this.unitPrice = itemHolder.getUnitPrice();
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }
}
