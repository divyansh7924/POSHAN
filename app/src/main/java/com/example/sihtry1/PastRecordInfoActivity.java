package com.example.sihtry1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.sihtry1.models.PastRecord;


public class PastRecordInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private PastRecord pastRecord = null;
    private TextView tv_guardianname, tv_childname, tv_gender, tv_dob, tv_bloodgrp, tv_treatedfor;
    Spinner pastrecsel;
    String selitem;
    Button showprog;


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
        tv_treatedfor = findViewById(R.id.past_rec_info_tv_treated_for);
        pastrecsel = findViewById(R.id.past_rec_info_sel);
        showprog = findViewById(R.id.past_rec_info_button);

        tv_guardianname.setText(pastRecord.getGuardian_name());
        tv_childname.setText(pastRecord.getChild_first_name() + " " + pastRecord.getChild_last_name());

        if (pastRecord.getChild_gender() == "f") {
            tv_gender.setText("Female");
        } else {
            tv_gender.setText("Male");
        }

        tv_dob.setText(pastRecord.getDay_of_birth() + "/" + pastRecord.getMonth_of_birth() + "/" + pastRecord.getYear_of_birth());
        tv_bloodgrp.setText(pastRecord.getBlood_group());

        showprog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showprogchild();
            }
        });

        tv_treatedfor.setText(pastRecord.getDisch_treated_for());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pastselect, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pastrecsel.setAdapter(adapter);
        pastrecsel.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selitem = pastrecsel.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void showprogchild() {
        Intent intent = new Intent(this, PastRecProgressActivity.class);
        intent.putExtra("selitem", selitem);
        intent.putExtra("PastRecord", pastRecord);
        startActivity(intent);
    }
}
