package com.tied.android.tiedapp.customs.model;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ClientDataModel {

    private String line_name;
    private String line_date;
    private String percent;
    private String price;

    public ClientDataModel(String line_name, String line_date, String percent, String price) {
        this.line_name = line_name;
        this.line_date = line_date;
        this.percent = percent;
        this.price = price;
    }

    public ClientDataModel() {
    }

    public String getLine_name() {
        return line_name;
    }

    public void setLine_name(String line_name) {
        this.line_name = line_name;
    }

    public String getLine_date() {
        return line_date;
    }

    public void setLine_date(String line_date) {
        this.line_date = line_date;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
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
                "line_name='" + line_name + '\'' +
                ", line_date='" + line_date + '\'' +
                ", percent=" + percent + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
