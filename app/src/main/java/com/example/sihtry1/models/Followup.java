package com.example.sihtry1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Followup {
    String nrc_id;
    @ServerTimestamp Date date_of_discharge;
    String referral_id;
    int num_followups;

    public Followup(){

    }

    public Followup(String nrc_id, Date date_of_discharge, String referral_id, int num_followups) {
        this.nrc_id = nrc_id;
        this.date_of_discharge = date_of_discharge;
        this.referral_id = referral_id;
        this.num_followups = num_followups;
    }

    public String getNrc_id() {
        return nrc_id;
    }

    public void setNrc_id(String nrc_id) {
        this.nrc_id = nrc_id;
    }

    public Date getDate_of_discharge() {
        return date_of_discharge;
    }

    public void setDate_of_discharge(Date date_of_discharge) {
        this.date_of_discharge = date_of_discharge;
    }

    public String getReferral_id() {
        return referral_id;
    }

    public void setReferral_id(String referral_id) {
        this.referral_id = referral_id;
    }

    public int getNum_followups() {
        return num_followups;
    }

    public void setNum_followups(int num_followups) {
        this.num_followups = num_followups;
    }
}
