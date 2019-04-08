package com.example.sihtry1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sihtry1.models.Admits;
import com.example.sihtry1.models.Followup;
import com.example.sihtry1.models.NRC;
import com.example.sihtry1.models.Referral;
import com.example.sihtry1.models.RCR;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FollowupChildActivity extends AppCompatActivity {

    private TextView tv_guardian_name, tv_child_name, tv_gender, tv_date_of_birth, tv_blood_group, tv_phone,
            tv_date_discharge, tv_address, tv_followups_done, tv_lastscreening;
    private EditText et_readmit_period;
    private Button btn_followup_done, btn_extend_followup, btn_readmit;
    private String followupDocId, rcrid, referralid;
    private int followupsdone, totalfollowups;
    private Date next_appointment;
    private CollectionReference refCollection;
    private FirebaseFirestore db;
    private ArrayList<Referral> mref = new ArrayList<>();
    private ArrayList<RCR> rcr = new ArrayList<>();
    private ArrayList<NRC> mNrc = new ArrayList<>();
    private String referralDocId, nrcId;
    private int bed_vacant;
    private Followup followup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_child);
        Intent intent = getIntent();
        followupDocId = intent.getStringExtra("followupDocId");
        followup = (Followup) intent.getSerializableExtra("followupObject");

        tv_guardian_name = findViewById(R.id.afc_tv_guardian_name);
        tv_child_name = findViewById(R.id.afc_tv_child_name);
        tv_gender = findViewById(R.id.afc_tv_child_gender);
        tv_date_of_birth = findViewById(R.id.afc_tv_date_of_birth);
        tv_blood_group = findViewById(R.id.afc_tv_blood_group);
        tv_phone = findViewById(R.id.afc_tv_phone);
        tv_date_discharge = findViewById(R.id.afc_tv_date_discharged);
        tv_address = findViewById(R.id.afc_tv_Address);
        tv_followups_done = findViewById(R.id.afc_tv_followups_done);
        tv_lastscreening = findViewById(R.id.afc_tv_screnning);
        et_readmit_period = findViewById(R.id.afc_et_readmit_period);
        btn_followup_done = findViewById(R.id.afc_btn_followup_done);
        btn_extend_followup = findViewById(R.id.afc_btn_extend_followup);
        btn_readmit = findViewById(R.id.afc_btn_readmit);


        db = FirebaseFirestore.getInstance();
        refCollection = db.collection("referral");

        btn_followup_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followupdone();
            }
        });
        btn_extend_followup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extend_followup();
            }
        });
        btn_readmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAdmit();
            }
        });


        tv_guardian_name.setText(followup.getGuardian_name());
        tv_followups_done.setText(String.valueOf(followup.getFollowups_done()));
        followupsdone = followup.getFollowups_done();
        if (followupsdone > 2) {
            btn_extend_followup.setVisibility(View.VISIBLE);
        }
        totalfollowups = followup.getTotal_followups();
        next_appointment = followup.getNext_date();

        tv_child_name.setText(followup.getChild_first_name() + " " + followup.getChild_last_name());
        tv_date_discharge.setText(String.valueOf(followup.getDate_of_discharge()));

        Calendar cal = GregorianCalendar.getInstance();
        Date next_date = followup.getNext_date();
        cal.setTime(next_date);
        cal.add(Calendar.DAY_OF_YEAR, -15);
        Date last_screening = cal.getTime();
        tv_lastscreening.setText(String.valueOf(last_screening));
        referralid = followup.getReferral_id();

        Query query = refCollection.whereEqualTo("referral_id", referralid);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Referral ref = documentSnapshot.toObject(Referral.class);
                        mref.add(ref);
                        referralDocId = documentSnapshot.getId();
                    }

                    tv_address.setText(mref.get(0).getVillage() + ", " + mref.get(0).getTehsil() + ", " + mref.get(0).getDistrict());
                    tv_gender.setText(mref.get(0).getChild_gender());
                    tv_date_of_birth.setText(mref.get(0).getDay_of_birth() + "/" + mref.get(0).getMonth_of_birth() + "/" + mref.get(0).getYear_of_birth());
                    tv_blood_group.setText(mref.get(0).getBlood_group());
                    tv_phone.setText(mref.get(0).getPhone());
                    rcrid = mref.get(0).getRcr_id();
                } else {
                    Toast.makeText(getApplicationContext(), "Ref not found", Toast.LENGTH_SHORT).show();
                }
            }
        });


        CollectionReference nrcRef = db.collection("nrc");
        nrcId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        query = nrcRef.whereEqualTo("user_id", nrcId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        NRC nrc = documentSnapshot.toObject(NRC.class);
                        mNrc.add(nrc);
                    }
                    bed_vacant = mNrc.get(0).getBed_vacant();
                    if (bed_vacant == 0) {
                        btn_readmit.setVisibility(View.INVISIBLE);
                        et_readmit_period.setFocusable(false);
                        et_readmit_period.setText("Beds Full can't admit more until any Discharge");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "NRC not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void followupdone() {
        if (followupsdone < 3) {
            followupsdone = followupsdone + 1;
            db.collection("Followup").document(followupDocId).update(
                    "followups_done", followupsdone
            );
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, 15);
            final Date next_date = cal.getTime();
            db.collection("Followup").document(followupDocId).update("next_date", next_date)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), followupsdone + "th Followup Done and next appointment given for " + next_date, Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            followupsdone = followupsdone + 1;
            db.collection("Followup").document(followupDocId).update(
                    "followups_done", followupsdone
            );
            db.collection("Followup").document(followupDocId).update(
                    "next_date", FieldValue.delete()
            );
            Toast.makeText(getApplicationContext(), followupsdone + "th Followup Done", Toast.LENGTH_LONG).show();
        }

        followupInfoActivity();
    }

    private void followupInfoActivity() {
        Intent intent = new Intent(FollowupChildActivity.this, FollowupInfoActivity.class);
        intent.putExtra("referralDocId", referralDocId);
        intent.putExtra("referral", mref.get(0));
        startActivity(intent);
    }

    private void extend_followup() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 15);
        final Date next_date = cal.getTime();
        db.collection("Followup").document(followupDocId).update("next_date", next_date)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        followupsdone = followupsdone + 1;
                        db.collection("Followup").document(followupDocId).update(
                                "followups_done", followupsdone
                        );
                        totalfollowups = totalfollowups + 1;
                        db.collection("Followup").document(followupDocId).update("total_followups", totalfollowups);
                        Toast.makeText(getApplicationContext(), followupsdone + "th followup done and Total followups for this child are now " + totalfollowups + ", next appointment on" + next_date, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), NRCActivity.class);
                        startActivity(intent);
                    }
                });
    }

    private void reAdmit() {
        db = FirebaseFirestore.getInstance();
        DocumentReference newAdmit = db.collection("Admit").document();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        int duration = Integer.parseInt(String.valueOf(et_readmit_period.getText()));
        Admits admit = new Admits();

        admit.setNrc_id(userid);
        admit.setReferral_id(referralid);
        admit.setDuration(duration);

        newAdmit.set(admit).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseFirestore db1;
                    db1 = FirebaseFirestore.getInstance();
                    db1.collection("referral").document(referralDocId).update(
                            "status", "Admitted"
                    );
                    db1.collection("Followup").document(followupDocId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("deleted followup ", followupDocId);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("error ", followupDocId + " ", e);
                                }
                            });
                    Toast.makeText(getApplicationContext(), "Admitted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), NRCActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Admission Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}