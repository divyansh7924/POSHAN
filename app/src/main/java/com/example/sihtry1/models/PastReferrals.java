package com.example.sihtry1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.Date;

public class PastReferrals implements Parcelable {
    String child_first_name;
    String referral_id;
    String child_last_name;
    String parent_name;
    String ncr_id;
    String rcr_id;
    Double aadhar_num;
    char gender;
    URL child_pic;
    Date dob;
    String symptoms;
    int asha_measure;
    int height;
    int weight;
    double phone;
    String state;
    String city;
    int pincode;
    String address;


    protected PastReferrals(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PastReferrals> CREATOR = new Creator<PastReferrals>() {
        @Override
        public PastReferrals createFromParcel(Parcel in) {
            return new PastReferrals(in);
        }

        @Override
        public PastReferrals[] newArray(int size) {
            return new PastReferrals[size];
        }
    };
}
