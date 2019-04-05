package com.example.sihtry1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sihtry1.models.Followup;
import com.example.sihtry1.models.NRC;
import com.example.sihtry1.models.Referral;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DischangeInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner_oedema;
    private int oedema_stage = -1, height, muac;
    private Float weight;
    private String treatedfor;
    private EditText et_muac, et_height, et_weight, et_treatedfor;
    private Button btn_submit;
    private FirebaseFirestore db;
    private Referral referral = null;
    private String refDocSnap, admitDocSnap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dischange_info);

        referral = (Referral) getIntent().getSerializableExtra("referral");
        refDocSnap = getIntent().getStringExtra("refDocSnap");
        admitDocSnap = getIntent().getStringExtra("admitDocSnap");

        spinner_oedema = findViewById(R.id.disch_info_spinner_oedema);
        et_height = findViewById(R.id.disch_info_et_height);
        et_weight = findViewById(R.id.disch_info_et_weight);
        et_muac = findViewById(R.id.disch_info_et_ashamsmt);
        et_treatedfor = findViewById(R.id.disch_info_et_treatedfor);
        btn_submit = findViewById(R.id.disch_info_btn_submit);

        db = FirebaseFirestore.getInstance();

        spinner_oedema.setOnItemSelectedListener(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_height.getText().toString().equals("")) {
                    Toast.makeText(DischangeInfoActivity.this, "Enter Height", Toast.LENGTH_SHORT).show();
                } else if (et_weight.getText().toString().equals("")) {
                    Toast.makeText(DischangeInfoActivity.this, "Enter Weight", Toast.LENGTH_SHORT).show();
                } else if (et_muac.getText().toString().equals("")) {
                    Toast.makeText(DischangeInfoActivity.this, "Enter MUAC", Toast.LENGTH_SHORT).show();
                } else if (oedema_stage < 0) {
                    Toast.makeText(DischangeInfoActivity.this, "Select Oedema Stage", Toast.LENGTH_SHORT).show();
                } else if (et_treatedfor.getText().toString().equals("")) {
                    Toast.makeText(DischangeInfoActivity.this, "Enter Treated For", Toast.LENGTH_SHORT).show();
                } else {
                    treatedfor = et_treatedfor.getText().toString();
                    weight = Float.parseFloat(et_weight.getText().toString());
                    height = Integer.parseInt(et_height.getText().toString());
                    muac = Integer.parseInt(et_muac.getText().toString());

                    bedWithdraw();
                    createNewDischarge();
                }
            }
        });
    }

    private void bedWithdraw() {
        CollectionReference nrcRef = db.collection("nrc");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = nrcRef.whereEqualTo("user_id", userId);

        final ArrayList<NRC> mNrc = new ArrayList<>();
        final DocumentReference[] documentReference = new DocumentReference[1];

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        NRC nrc = documentSnapshot.toObject(NRC.class);
                        mNrc.add(nrc);
                        documentReference[0] = documentSnapshot.getReference();

                        documentReference[0].update("bed_vacant", mNrc.get(0).getBed_vacant() + 1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Bed Vacant changed", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Bed Vacant couldn't change", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "NRC not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNewDischarge() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newFollowup = db.collection("Followup").document();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Followup followup = new Followup();

        followup.setNrc_id(userid);
        followup.setTotal_followups(4);
        followup.setFollowups_done(0);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 15);
        Date next_date = cal.getTime();

        followup.setNext_date(next_date);
        followup.setReferral_id(referral.getReferral_id());
        followup.setNrc_id(userid);
        followup.setChild_first_name(referral.getChild_first_name());
        followup.setChild_last_name(referral.getChild_last_name());
        followup.setGuardian_name(referral.getGuadian_name());

        newFollowup.set(followup).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseFirestore db1;
                    db1 = FirebaseFirestore.getInstance();
                    db1.collection("referral").document(refDocSnap).update(
                            "status", "Discharged"
                    );
                    db1.collection("Admit").document(admitDocSnap)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("deleted admit document ", admitDocSnap);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("error ", admitDocSnap + " ", e);
                                }
                            });

                    Toast.makeText(getApplicationContext(), "Discharged", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), NRCActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Discharge Failed", Toast.LENGTH_SHORT).show();
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
}
