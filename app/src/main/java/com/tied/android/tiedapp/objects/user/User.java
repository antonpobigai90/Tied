package com.tied.android.tiedapp.objects.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tied.android.tiedapp.customs.Constants;

import java.io.Serializable;

/**
 * Created by Emmanuel on 5/28/2016.
 */
public class User implements Serializable {
    public static final String TAG = User.class.getSimpleName();

    private String id;
    private String email;
    private String first_name;
    private String last_name;
    private String phone;
    private String fax;
    private String password;
    private String avatar;

    private int sign_up_stage;

    public Location home_address;
    public Location office_address;

    public Boss boss;

    public Profile profile;

    private String createdAt;
    private String updatedAt;

    private String token;

    public User(String id, String email, String first_name, String last_name, String phone,
                String fax, String password, String avatar, int sign_up_stage, Location home_address,
                Location office_address, Boss boss, Profile profile, String createdAt, String updatedAt, String token) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.fax = fax;
        this.password = password;
        this.avatar = avatar;
        this.sign_up_stage = sign_up_stage;
        this.home_address = home_address;
        this.office_address = office_address;
        this.boss = boss;
        this.profile = profile;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.token = token;
    }

    public User(String id, String email, String token, int sign_up_stage) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.sign_up_stage = sign_up_stage;
    }

    public User() {
    }



    public static User getUser(Context context){
        Gson gson = new Gson();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = mPrefs.getString(Constants.CURRENT_USER, "");
        return gson.fromJson(json, User.class);
    }

    public boolean save(Context context){
        User user = this;

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user); // myObject - instance of MyObject
        prefsEditor.putString(Constants.CURRENT_USER, json);
        prefsEditor.apply();
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSign_up_stage() {
        return sign_up_stage;
    }

    public void setSign_up_stage(int sign_up_stage) {
        this.sign_up_stage = sign_up_stage;
    }

    public Location getHome_address() {
        return home_address;
    }

    public void setHome_address(Location home_address) {
        this.home_address = home_address;
    }

    public Location getOffice_address() {
        return office_address;
    }

    public void setOffice_address(Location office_address) {
        this.office_address = office_address;
    }

    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sign_up_stage=" + sign_up_stage +
                ", home_address=" + home_address +
                ", office_address=" + office_address +
                ", boss=" + boss +
                ", profile=" + profile +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
