package com.tied.android.tiedapp.customs.model;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ClientDataModel {

    private String client_name;
    private String month;
    private String price;

    public ClientDataModel(String client_name, String month, String price) {
        this.client_name = client_name;
        this.month = month;
        this.price = price;
    }

    public ClientDataModel() {
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ClientDataModel{" +
                "client_name='" + client_name + '\'' +
                ", month='" + month + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
