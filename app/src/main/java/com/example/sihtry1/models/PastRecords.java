package com.example.sihtry1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class PastRecords {
    //deleted data of admit and followup and health will be added here
    int admit_dur;
    Date date_admission;
    ArrayList<Date> followup_dates;
    int num_of_followups;
    // health related stuff

    Date date_screened;

    String referral_id;
    String child_first_name;
    String child_last_name;
    String child_gender;
    String child_aadhaar_num;
    int day_of_birth;
    int month_of_birth;
    int year_of_birth;
    String blood_group;
    float asha_measure; // array
    float height; // array
    float weight; // array
    int oedema; // array

    String guardian_name;
    String guardian_aadhaar_num;

    String nrc_id;
    String rcr_id;

    String other_symptoms; // array
    String treated_for; // array

    String phone;
    String village;
    String state;
    String tehsil;
    String district;
    int pincode;
}
