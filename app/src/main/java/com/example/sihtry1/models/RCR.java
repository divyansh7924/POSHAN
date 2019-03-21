package com.example.sihtry1.models;

public class RCR {
    String user_id;
    String reg_certi;
    String title;
    String address;
    String state;
    String reg_num;
    String city;
    int pincode;
    String phone;
    String email;
    boolean verified;

    double lat, lon;

    public RCR() {

    }

    public RCR(String user_id, String reg_certi, String title, String address, String state, String reg_num, String city, int pincode, String phone, String email, boolean verified, double lat, double lon) {
        this.user_id = user_id;
        this.reg_certi = reg_certi;
        this.title = title;
        this.address = address;
        this.state = state;
        this.reg_num = reg_num;
        this.city = city;
        this.pincode = pincode;
        this.phone = phone;
        this.email = email;
        this.verified = verified;
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReg_certi() {
        return reg_certi;
    }

    public void setReg_certi(String reg_certi) {
        this.reg_certi = reg_certi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getReg_num() {
        return reg_num;
    }

    public void setReg_num(String reg_num) {
        this.reg_num = reg_num;
    }
}
