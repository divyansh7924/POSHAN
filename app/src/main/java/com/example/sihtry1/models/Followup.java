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
    int total_followups;
    int followups_done;
    Date next_date;
    String child_first_name;
    String child_last_name;
    String guardian_name;

    public Followup(){

    }

    public Followup(String nrc_id, Date date_of_discharge, String referral_id, int total_followups, int followups_done, Date next_date, String child_first_name, String child_last_name, String guardian_name) {
        this.nrc_id = nrc_id;
        this.date_of_discharge = date_of_discharge;
        this.referral_id = referral_id;
        this.total_followups = total_followups;
        this.followups_done = followups_done;
        this.next_date = next_date;
        this.child_first_name = child_first_name;
        this.child_last_name = child_last_name;
        this.guardian_name = guardian_name;
    }

    public int getFollowups_done() {
        return followups_done;
    }

    public void setFollowups_done(int followups_done) {
        this.followups_done = followups_done;
    }

    public int getTotal_followups() {
        return total_followups;
    }

    public void setTotal_followups(int total_followups) {
        this.total_followups = total_followups;
    }

    public Date getNext_date() {
        return next_date;
    }

    public void setNext_date(Date next_date) {
        this.next_date = next_date;
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

    public String getGuardian_name() {
        return guardian_name;
    }

    public void setGuardian_name(String guardian_name) {
        this.guardian_name = guardian_name;
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

}
