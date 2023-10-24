package com.example.orixpense.Model;

public class Data {
    private int amount;
    private String id;
    private String category;
    private String description;

    public Data(int amount, String id, String category, String description, String date) {
        this.amount = amount;
        this.id = id;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public Data(){

    }

}
