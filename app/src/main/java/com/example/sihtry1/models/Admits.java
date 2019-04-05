package com.example.sihtry1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Admits implements Serializable {
    String nrc_id;
    String referral_id;
    int duration;
    @ServerTimestamp Date date_of_admission;

    public Admits(){

    }

    public Admits(String nrc_id, String referral_id, int duration, Date date_of_admission) {
        this.nrc_id = nrc_id;
        this.referral_id = referral_id;
        this.duration = duration;
        this.date_of_admission = date_of_admission;
    }

    public String getNrc_id() {
        return nrc_id;
    }

    public void setNrc_id(String nrc_id) {
        this.nrc_id = nrc_id;
    }

    public String getReferral_id() {
        return referral_id;
    }

    public void setReferral_id(String referral_id) {
        this.referral_id = referral_id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getDate_of_admission() {
        return date_of_admission;
    }

    public void setDate_of_admission(Date date_of_admission) {
        this.date_of_admission = date_of_admission;
    }
}
