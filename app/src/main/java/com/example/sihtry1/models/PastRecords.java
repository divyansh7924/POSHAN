package com.example.sihtry1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class PastRecords {
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
    ArrayList<Integer> fllw_weight;
    ArrayList<Integer> fllw_oedema;
    ArrayList<String> fllw_other_symptoms;
}
