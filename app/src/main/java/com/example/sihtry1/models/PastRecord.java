package com.example.sihtry1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class PastRecord implements Serializable {
    //basic info
    String referral_id;
    String child_first_name;
    String child_last_name;
    String child_gender;
    String child_aadhaar_num;
    int day_of_birth;
    int month_of_birth;
    int year_of_birth;
    String blood_group;
    String guardian_name;
    String guardian_aadhaar_num;
    String nrc_id;
    String rcr_id;
    String phone;
    String village;
    String state;
    String tehsil;
    String district;
    int pincode;

    //admission details
    int admit_dur;
    Date admit_date;
    float admit_asha_measure;
    float admit_height;
    float admit_weight;
    int admit_oedema;
    String admit_other_symptoms;
    String admit_treated_for;

    //discharge
    float disch_asha_measure;
    float disch_height;
    float disch_weight;
    int disch_oedema;
    String disch_treated_for;

    //followup details
    int num_of_followups;
    ArrayList<Date> followup_dates;
    ArrayList<Integer> fllw_asha_measure;
    ArrayList<Integer> fllw_height;
    ArrayList<Float> fllw_weight;
    ArrayList<Integer> fllw_oedema;
    ArrayList<String> fllw_other_symptoms;

    public PastRecord() {

    }

    public PastRecord(String referral_id, String child_first_name, String child_last_name,
                      String child_gender, String child_aadhaar_num, int day_of_birth, int month_of_birth,
                      int year_of_birth, String blood_group, String guardian_name, String guardian_aadhaar_num,
                      String nrc_id, String rcr_id, String phone, String village, String state,
                      String tehsil, String district, int pincode, int admit_dur, Date admit_date,
                      float admit_asha_measure, float admit_height, float admit_weight, int admit_oedema,
                      String admit_other_symptoms, String admit_treated_for, float disch_asha_measure,
                      float disch_height, float disch_weight, int disch_oedema, String disch_treated_for,
                      int num_of_followups, ArrayList<Date> followup_dates, ArrayList<Integer> fllw_asha_measure,
                      ArrayList<Integer> fllw_height, ArrayList<Float> fllw_weight, ArrayList<Integer> fllw_oedema,
                      ArrayList<String> fllw_other_symptoms) {
        this.referral_id = referral_id;
        this.child_first_name = child_first_name;
        this.child_last_name = child_last_name;
        this.child_gender = child_gender;
        this.child_aadhaar_num = child_aadhaar_num;
        this.day_of_birth = day_of_birth;
        this.month_of_birth = month_of_birth;
        this.year_of_birth = year_of_birth;
        this.blood_group = blood_group;
        this.guardian_name = guardian_name;
        this.guardian_aadhaar_num = guardian_aadhaar_num;
        this.nrc_id = nrc_id;
        this.rcr_id = rcr_id;
        this.phone = phone;
        this.village = village;
        this.state = state;
        this.tehsil = tehsil;
        this.district = district;
        this.pincode = pincode;
        this.admit_dur = admit_dur;
        this.admit_date = admit_date;
        this.admit_asha_measure = admit_asha_measure;
        this.admit_height = admit_height;
        this.admit_weight = admit_weight;
        this.admit_oedema = admit_oedema;
        this.admit_other_symptoms = admit_other_symptoms;
        this.admit_treated_for = admit_treated_for;
        this.disch_asha_measure = disch_asha_measure;
        this.disch_height = disch_height;
        this.disch_weight = disch_weight;
        this.disch_oedema = disch_oedema;
        this.disch_treated_for = disch_treated_for;
        this.num_of_followups = num_of_followups;
        this.followup_dates = followup_dates;
        this.fllw_asha_measure = fllw_asha_measure;
        this.fllw_height = fllw_height;
        this.fllw_weight = fllw_weight;
        this.fllw_oedema = fllw_oedema;
        this.fllw_other_symptoms = fllw_other_symptoms;
    }

    public String getReferral_id() {
        return referral_id;
    }

    public void setReferral_id(String referral_id) {
        this.referral_id = referral_id;
    }

    public String getChild_first_name() {
        return child_first_name;
    }

    public void setChild_first_name(String child_first_name) {
        this.child_first_name = child_first_name;
    }

    public String getChild_last_name() {
        return child_last_name;
    }

    public void setChild_last_name(String child_last_name) {
        this.child_last_name = child_last_name;
    }

    public String getChild_gender() {
        return child_gender;
    }

    public void setChild_gender(String child_gender) {
        this.child_gender = child_gender;
    }

    public String getChild_aadhaar_num() {
        return child_aadhaar_num;
    }

    public void setChild_aadhaar_num(String child_aadhaar_num) {
        this.child_aadhaar_num = child_aadhaar_num;
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

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getGuardian_name() {
        return guardian_name;
    }

    public void setGuardian_name(String guardian_name) {
        this.guardian_name = guardian_name;
    }

    public String getGuardian_aadhaar_num() {
        return guardian_aadhaar_num;
    }

    public void setGuardian_aadhaar_num(String guardian_aadhaar_num) {
        this.guardian_aadhaar_num = guardian_aadhaar_num;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public int getAdmit_dur() {
        return admit_dur;
    }

    public void setAdmit_dur(int admit_dur) {
        this.admit_dur = admit_dur;
    }

    public Date getAdmit_date() {
        return admit_date;
    }

    public void setAdmit_date(Date admit_date) {
        this.admit_date = admit_date;
    }

    public float getAdmit_asha_measure() {
        return admit_asha_measure;
    }

    public void setAdmit_asha_measure(float admit_asha_measure) {
        this.admit_asha_measure = admit_asha_measure;
    }

    public float getAdmit_height() {
        return admit_height;
    }

    public void setAdmit_height(float admit_height) {
        this.admit_height = admit_height;
    }

    public float getAdmit_weight() {
        return admit_weight;
    }

    public void setAdmit_weight(float admit_weight) {
        this.admit_weight = admit_weight;
    }

    public int getAdmit_oedema() {
        return admit_oedema;
    }

    public void setAdmit_oedema(int admit_oedema) {
        this.admit_oedema = admit_oedema;
    }

    public String getAdmit_other_symptoms() {
        return admit_other_symptoms;
    }

    public void setAdmit_other_symptoms(String admit_other_symptoms) {
        this.admit_other_symptoms = admit_other_symptoms;
    }

    public String getAdmit_treated_for() {
        return admit_treated_for;
    }

    public void setAdmit_treated_for(String admit_treated_for) {
        this.admit_treated_for = admit_treated_for;
    }

    public float getDisch_asha_measure() {
        return disch_asha_measure;
    }

    public void setDisch_asha_measure(float disch_asha_measure) {
        this.disch_asha_measure = disch_asha_measure;
    }

    public float getDisch_height() {
        return disch_height;
    }

    public void setDisch_height(float disch_height) {
        this.disch_height = disch_height;
    }

    public float getDisch_weight() {
        return disch_weight;
    }

    public void setDisch_weight(float disch_weight) {
        this.disch_weight = disch_weight;
    }

    public int getDisch_oedema() {
        return disch_oedema;
    }

    public void setDisch_oedema(int disch_oedema) {
        this.disch_oedema = disch_oedema;
    }

    public String getDisch_treated_for() {
        return disch_treated_for;
    }

    public void setDisch_treated_for(String disch_treated_for) {
        this.disch_treated_for = disch_treated_for;
    }

    public int getNum_of_followups() {
        return num_of_followups;
    }

    public void setNum_of_followups(int num_of_followups) {
        this.num_of_followups = num_of_followups;
    }

    public ArrayList<Date> getFollowup_dates() {
        return followup_dates;
    }

    public void setFollowup_dates(ArrayList<Date> followup_dates) {
        this.followup_dates = followup_dates;
    }

    public ArrayList<Integer> getFllw_asha_measure() {
        return fllw_asha_measure;
    }

    public void setFllw_asha_measure(ArrayList<Integer> fllw_asha_measure) {
        this.fllw_asha_measure = fllw_asha_measure;
    }

    public ArrayList<Integer> getFllw_height() {
        return fllw_height;
    }

    public void setFllw_height(ArrayList<Integer> fllw_height) {
        this.fllw_height = fllw_height;
    }

    public ArrayList<Float> getFllw_weight() {
        return fllw_weight;
    }

    public void setFllw_weight(ArrayList<Float> fllw_weight) {
        this.fllw_weight = fllw_weight;
    }

    public ArrayList<Integer> getFllw_oedema() {
        return fllw_oedema;
    }

    public void setFllw_oedema(ArrayList<Integer> fllw_oedema) {
        this.fllw_oedema = fllw_oedema;
    }

    public ArrayList<String> getFllw_other_symptoms() {
        return fllw_other_symptoms;
    }

    public void setFllw_other_symptoms(ArrayList<String> fllw_other_symptoms) {
        this.fllw_other_symptoms = fllw_other_symptoms;
    }
}
