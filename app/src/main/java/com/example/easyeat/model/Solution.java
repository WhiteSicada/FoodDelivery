package com.example.easyeat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;



public class Solution implements Serializable
{
    public Category category;
    public ArrayList<SubCategory> subCategoryList;
    public ArrayList<Item> itemList;
    public Map<SubCategory, ArrayList<Item>> itemMap;

    public Solution(Category category, ArrayList<SubCategory> subCategoryList, ArrayList<Item> itemList, Map<SubCategory, ArrayList<Item>> itemMap)
    {
        this.category = category;
        this.subCategoryList = subCategoryList;
        this.itemList = itemList;
        this.itemMap = itemMap;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ArrayList<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(ArrayList<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public Map<SubCategory, ArrayList<Item>> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<SubCategory, ArrayList<Item>> itemMap) {
        this.itemMap = itemMap;
    }
}
