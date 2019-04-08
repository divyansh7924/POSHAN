package com.example.sihtry1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sihtry1.models.Admits;
import com.example.sihtry1.models.PastRecord;
import com.example.sihtry1.models.Referral;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class FollowupInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner_oedema;
    private int oedema_stage = -1, height, muac;
    private Float weight;
    private String othersym;
    private EditText et_muac, et_height, et_weight, et_othersym;
    private Button btn_submit;
    private FirebaseFirestore db;
    private Referral referral = null;
    private String refDocId, admitDocSnap, pastRecordDocId;
    private PastRecord pastRecord = null;
    private Admits admit = null;

    private int num_of_followups;
    private ArrayList<Date> followup_dates;
    private ArrayList<Integer> fllw_asha_measure;
    private ArrayList<Integer> fllw_height;
    private ArrayList<Float> fllw_weight;
    private ArrayList<Integer> fllw_oedema;
    private ArrayList<String> fllw_other_symptoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_info);

        referral = (Referral) getIntent().getSerializableExtra("referral");
        refDocId = getIntent().getStringExtra("referralDocId");

        spinner_oedema = findViewById(R.id.followup_info_spinner_oedema);
        et_height = findViewById(R.id.followup_info_et_height);
        et_weight = findViewById(R.id.followup_info_et_weight);
        et_muac = findViewById(R.id.followup_info_et_ashamsmt);
        et_othersym = findViewById(R.id.followup_info_et_othersym);
        btn_submit = findViewById(R.id.followup_info_btn_submit);

        spinner_oedema.setOnItemSelectedListener(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_height.getText().toString().equals("")) {
                    Toast.makeText(FollowupInfoActivity.this, "Enter Height", Toast.LENGTH_SHORT).show();
                } else if (et_weight.getText().toString().equals("")) {
                    Toast.makeText(FollowupInfoActivity.this, "Enter Weight", Toast.LENGTH_SHORT).show();
                } else if (et_muac.getText().toString().equals("")) {
                    Toast.makeText(FollowupInfoActivity.this, "Enter MUAC", Toast.LENGTH_SHORT).show();
                } else if (oedema_stage < 0) {
                    Toast.makeText(FollowupInfoActivity.this, "Select Oedema Stage", Toast.LENGTH_SHORT).show();
                } else if (et_othersym.getText().toString().equals("")) {
                    Toast.makeText(FollowupInfoActivity.this, "Enter Treated For", Toast.LENGTH_SHORT).show();
                } else {
                    othersym = et_othersym.getText().toString();
                    weight = Float.parseFloat(et_weight.getText().toString());
                    height = Integer.parseInt(et_height.getText().toString());
                    muac = Integer.parseInt(et_muac.getText().toString());

                    updatePastRecord();
                    openNRCActivity();
                }
            }
        });

        final ArrayList<PastRecord> mPastRecord = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        final String[] pastRecordDocIdtemp = new String[1];

        CollectionReference pastRecordCollection = db.collection("PastRecord");
        Query query = pastRecordCollection.whereEqualTo("referral_id", referral.getReferral_id());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(FollowupInfoActivity.this, "Past Record Retreived", Toast.LENGTH_SHORT).show();

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        PastRecord pastRecord = documentSnapshot.toObject(PastRecord.class);
                        mPastRecord.add(pastRecord);
                        pastRecordDocIdtemp[0] = documentSnapshot.getId();
                    }

                    pastRecord = mPastRecord.get(0);
                    pastRecordDocId = pastRecordDocIdtemp[0];

                } else {
                    Toast.makeText(getApplicationContext(), "Ref not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePastRecord() {
        num_of_followups = pastRecord.getNum_of_followups();
        followup_dates = pastRecord.getFollowup_dates();
        fllw_asha_measure = pastRecord.getFllw_asha_measure();
        fllw_height = pastRecord.getFllw_height();
        fllw_weight = pastRecord.getFllw_weight();
        fllw_oedema = pastRecord.getFllw_oedema();
        fllw_other_symptoms = pastRecord.getFllw_other_symptoms();

        num_of_followups++;
        if (followup_dates == null) {
            followup_dates = new ArrayList<>();
        }
        followup_dates.add(new Date());
        if (fllw_asha_measure == null) {
            fllw_asha_measure = new ArrayList<>();
        }
        fllw_asha_measure.add(muac);
        if (fllw_height == null) {
            fllw_height = new ArrayList<>();
        }
        fllw_height.add(height);
        if (fllw_weight == null) {
            fllw_weight = new ArrayList<>();
        }
        fllw_weight.add(weight);
        if (fllw_oedema == null) {
            fllw_oedema = new ArrayList<>();
        }
        fllw_oedema.add(oedema_stage);
        if (fllw_other_symptoms == null) {
            fllw_other_symptoms = new ArrayList<>();
        }
        fllw_other_symptoms.add(othersym);

        db.collection("PastRecord").document(pastRecordDocId).update(
                "num_of_followups", num_of_followups, "followup_dates", followup_dates,
                "fllw_asha_measure", fllw_asha_measure, "fllw_height", fllw_height, "fllw_weight", fllw_weight,
                "fllw_oedema", fllw_oedema, "fllw_other_symptoms", fllw_other_symptoms)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(FollowupInfoActivity.this, "Past Referra, Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FollowupInfoActivity.this, "Past Referra, Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        oedema_stage = position - 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void openNRCActivity() {
        Toast.makeText(getApplicationContext(), "Discharged", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), NRCActivity.class);
        startActivity(intent);
        finish();
    }
}
