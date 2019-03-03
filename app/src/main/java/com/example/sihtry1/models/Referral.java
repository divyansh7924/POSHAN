package com.example.sihtry1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nullable;

@IgnoreExtraProperties
public class Referral {
    String child_first_name;
    String referral_id;
    String child_last_name;
    String guadian_name;
    String nrc_id;
    String rcr_id;
    String guardian_aadhar_num;
    String child_gender;
    int day_of_birth;
    int month_of_birth;
    int year_of_birth;
    String symptoms;
    String blood_group;
    float asha_measure;
    float height;
    float weight;
    String phone;
    String state;
    String city;
    int pincode;
    String address;

    public Referral() {
    }

    public Referral(String child_first_name, String referral_id, String child_last_name, String guadian_name,
                    String nrc_id, String rcr_id, String guardian_aadhar_num, String child_gender,
                    int day_of_birth, int month_of_birth, int year_of_birth, String symptoms, String blood_group,
                    float asha_measure, float height, float weight, String phone, String state, String city, int pincode, String address) {
        this.child_first_name = child_first_name;
        this.referral_id = referral_id;
        this.child_last_name = child_last_name;
        this.guadian_name = guadian_name;
        this.nrc_id = nrc_id;
        this.rcr_id = rcr_id;
        this.guardian_aadhar_num = guardian_aadhar_num;
        this.child_gender = child_gender;
        this.day_of_birth = day_of_birth;
        this.month_of_birth = month_of_birth;
        this.year_of_birth = year_of_birth;
        this.symptoms = symptoms;
        this.blood_group = blood_group;
        this.asha_measure = asha_measure;
        this.height = height;
        this.weight = weight;
        this.phone = phone;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.address = address;
    }

    public String getChild_first_name() {
        return child_first_name;
    }

    public void setChild_first_name(String child_first_name) {
        this.child_first_name = child_first_name;
    }

    public String getReferral_id() {
        return referral_id;
    }

    public void setReferral_id(String referral_id) {
        this.referral_id = referral_id;
    }

    public String getChild_last_name() {
        return child_last_name;
    }

    public void setChild_last_name(String child_last_name) {
        this.child_last_name = child_last_name;
    }

    public String getGuadian_name() {
        return guadian_name;
    }

    public void setGuadian_name(String guadian_name) {
        this.guadian_name = guadian_name;
    }

    public String getNrc_id() {
        return nrc_id;
    }

    public void setNrc_id(String nrc_id) {
        this.nrc_id = nrc_id;
    }

    public String getRcr_id() {
        return rcr_id;
    }

    public void setRcr_id(String rcr_id) {
        this.rcr_id = rcr_id;
    }

    public String getGuardian_aadhar_num() {
        return guardian_aadhar_num;
    }

    public void setGuardian_aadhar_num(String guardian_aadhar_num) {
        this.guardian_aadhar_num = guardian_aadhar_num;
    }

    public String getChild_gender() {
        return child_gender;
    }

    public void setChild_gender(String child_gender) {
        this.child_gender = child_gender;
    }

    public int getDay_of_birth() {
        return day_of_birth;
    }

    public void setDay_of_birth(int day_of_birth) {
        this.day_of_birth = day_of_birth;
    }

    public int getMonth_of_birth() {
        return month_of_birth;
    }

    public void setMonth_of_birth(int month_of_birth) {
        this.month_of_birth = month_of_birth;
    }

    public int getYear_of_birth() {
        return year_of_birth;
    }

    public void setYear_of_birth(int year_of_birth) {
        this.year_of_birth = year_of_birth;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public float getAsha_measure() {
        return asha_measure;
    }

    public void setAsha_measure(float asha_measure) {
        this.asha_measure = asha_measure;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
