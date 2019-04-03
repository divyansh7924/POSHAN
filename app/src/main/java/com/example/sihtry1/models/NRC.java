package com.example.sihtry1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.common.internal.HideFirstParty;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class NRC implements Serializable {
    String user_id;
    int bed_count;
    String email;
    int bed_vacant;
    String title;
    String reg_certi;
    String reg_num;
    String address;
    String state;
    String city;
    int pincode;
    String phone;
    boolean verified;
    double lat, lon;

    @Exclude double distance;

    public NRC() {
    }


    public NRC(String user_id, int bed_count, String email, int bed_vacant, String title,
               String reg_certi, String reg_num, String address, String state, String city,
               int pincode, String phone, boolean verified, double lat, double lon) {
        this.user_id = user_id;
        this.bed_count = bed_count;
        this.email = email;
        this.bed_vacant = bed_vacant;
        this.title = title;
        this.reg_certi = reg_certi;
        this.reg_num = reg_num;
        this.address = address;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.phone = phone;
        this.verified = verified;
        this.lat = lat;
        this.lon = lon;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getBed_count() {
        return bed_count;
    }

    public void setBed_count(int bed_count) {
        this.bed_count = bed_count;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBed_vacant() {
        return bed_vacant;
    }

    public void setBed_vacant(int bed_vacant) {
        this.bed_vacant = bed_vacant;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReg_certi() {
        return reg_certi;
    }

    public void setReg_certi(String reg_certi) {
        this.reg_certi = reg_certi;
    }

    public String getReg_num() {
        return reg_num;
    }

    public void setReg_num(String reg_num) {
        this.reg_num = reg_num;
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
