package com.tied.android.tiedapp.objects;

import java.io.Serializable;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class Client implements Serializable {

    public static final String TAG = Client.class.getSimpleName();

    private String id;
    private String user_id;
    private String line_id;
    private String full_name;
    private String company;
    private String logo;
    private String description;
    private String phone;
    private String email;
    private Location address;
    private String birthday;

    private int Industry_id;
    private int visit_id;

    int image;

    public Client() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLine_id() {
        return line_id;
    }

    public void setLine_id(String line_id) {
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

    public int getIndustry_id() {
        return Industry_id;
    }

    public void setIndustry_id(int industry_id) {
        Industry_id = industry_id;
    }

    public int getVisit_id() {
        return visit_id;
    }

    public void setVisit_id(int visit_id) {
        this.visit_id = visit_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", line_id='" + line_id + '\'' +
                ", full_name='" + full_name + '\'' +
                ", company='" + company + '\'' +
                ", logo='" + logo + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", birthday='" + birthday + '\'' +
                ", Industry_id=" + Industry_id +
                ", visit_id=" + visit_id +
                ", image=" + image +
                '}';
    }
}
