package com.example.virtuallibrary.models;

public class CategoriesCount {
    private String categories;
    private Long count;

    public CategoriesCount(String categories, Long count) {
        this.categories = categories;
        this.count = count;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
    
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
