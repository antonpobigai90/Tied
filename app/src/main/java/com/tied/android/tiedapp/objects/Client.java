package com.tied.android.tiedapp.objects;

import java.io.Serializable;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class Client implements Serializable {

    public static final String TAG = Client.class.getSimpleName();

    private int user_id;
    private int line_id;
    private String full_name;
    private String company;
    private String logo;
    private String description;
    private String phone;
    private String email;
    private Location address;

    int image;

    public Client() {
    }

    public Client(int user_id, int line_id, String full_name, String company,
                  String logo, String description, String phone, String email, Location address) {
        this.user_id = user_id;
        this.line_id = line_id;
        this.full_name = full_name;
        this.company = company;
        this.logo = logo;
        this.description = description;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public Client(String full_name,int image, Location address){
        this.full_name = full_name;
        this.image = image;
        this.address = address;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getLine_id() {
        return line_id;
    }

    public void setLine_id(int line_id) {
        this.line_id = line_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }

    //for dummy images
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Client{" +
                "user_id=" + user_id +
                ", line_id=" + line_id +
                ", full_name='" + full_name + '\'' +
                ", company='" + company + '\'' +
                ", logo='" + logo + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                '}';
    }

}
