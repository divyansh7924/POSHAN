package com.example.sihtry1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sihtry1.models.PastRecord;

public class PastRecordInfoActivity extends AppCompatActivity {
    private PastRecord pastRecord = null;
    private TextView tv_guardianname, tv_childname, tv_gender, tv_dob, tv_bloodgrp, tv_muac, tv_height,
            tv_weight, tv_oedema, tv_treatedfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pastRecord = (PastRecord) getIntent().getSerializableExtra("PastRecord");
        setContentView(R.layout.activity_past_record_info);

        pastRecord = (PastRecord) getIntent().getSerializableExtra("PastRecord");
        tv_guardianname = findViewById(R.id.past_rec_info_tv_guardian_name);
        tv_childname = findViewById(R.id.past_rec_info_tv_child_name);
        tv_gender = findViewById(R.id.past_rec_info_tv_child_gender);
        tv_dob = findViewById(R.id.past_rec_info_tv_date_of_birth);
        tv_bloodgrp = findViewById(R.id.past_rec_info_tv_blood_group);
        tv_muac = findViewById(R.id.past_rec_info_tv_asha_tape);
        tv_height = findViewById(R.id.past_rec_info_tv_height);
        tv_weight = findViewById(R.id.past_rec_info_tv_weight);
        tv_oedema = findViewById(R.id.past_rec_info_tv_oedema);
        tv_treatedfor = findViewById(R.id.past_rec_info_tv_treated_for);

        tv_guardianname.setText(pastRecord.getGuardian_name());
        tv_childname.setText(pastRecord.getChild_first_name() + " " + pastRecord.getChild_last_name());

        if (pastRecord.getChild_gender() == "f") {
            tv_gender.setText("Female");
        } else {
            tv_gender.setText("Male");
        }

        tv_dob.setText(pastRecord.getDay_of_birth() + "/" + pastRecord.getMonth_of_birth() + "/" + pastRecord.getYear_of_birth());
        tv_bloodgrp.setText(pastRecord.getBlood_group());
        tv_muac.setText(String.valueOf(pastRecord.getDisch_asha_measure()));
        tv_height.setText(String.valueOf(pastRecord.getDisch_height()));
        tv_weight.setText(String.valueOf(pastRecord.getDisch_weight()));

        if (pastRecord.getDisch_oedema() == 0) {
            tv_oedema.setText("0");
        } else if (pastRecord.getDisch_oedema() == 1) {
            tv_oedema.setText("+");
        } else if (pastRecord.getDisch_oedema() == 2) {
            tv_oedema.setText("++");
        } else {
            tv_oedema.setText("+++");
        }

        tv_treatedfor.setText(pastRecord.getDisch_treated_for());
    }
}
