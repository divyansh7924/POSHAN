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
    private String selectedchild, rcrid, referralid;
    int followupsdone, totalfollowups;
    Date next_appointment;
    private CollectionReference referraldetails;
    FirebaseFirestore db;
    private ArrayList<Referral> mref = new ArrayList<>();
    private ArrayList<RCR> rcr = new ArrayList<>();
    String referraldocid,followupdocsnap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_child);
        Intent intent = getIntent();
        selectedchild = intent.getStringExtra("docid");

        tv_guardian_name = (TextView) findViewById(R.id.afc_tv_guardian_name);
        tv_child_name = (TextView) findViewById(R.id.afc_tv_child_name);
        tv_gender = (TextView) findViewById(R.id.afc_tv_child_gender);
        tv_date_of_birth = (TextView) findViewById(R.id.afc_tv_date_of_birth);
        tv_blood_group = (TextView) findViewById(R.id.afc_tv_blood_group);
        tv_phone = (TextView) findViewById(R.id.afc_tv_phone);
        tv_date_discharge = findViewById(R.id.afc_tv_date_discharged);
        tv_address = findViewById(R.id.afc_tv_Address);
        tv_followups_done = findViewById(R.id.afc_tv_followups_done);
        tv_lastscreening = findViewById(R.id.afc_tv_screnning);
        et_readmit_period = findViewById(R.id.afc_et_readmit_period);
        btn_followup_done = findViewById(R.id.afc_btn_followup_done);
        btn_extend_followup = findViewById(R.id.afc_btn_extend_followup);
        btn_readmit = findViewById(R.id.afc_btn_readmit);


        db = FirebaseFirestore.getInstance();
        referraldetails = db.collection("referral");
        final DocumentReference docRef = db.collection("Followup").document(selectedchild);

        final Followup[] Followup = {null};
        btn_followup_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Followupdone();
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
                readmit();
            }
        });


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Followup[0] = task.getResult().toObject(Followup.class);
                    if (Followup[0] == null) {
                        Toast.makeText(getApplicationContext(), "null  ", Toast.LENGTH_SHORT).show();
                    } else {
                        followupdocsnap = docRef.getId();
                        tv_guardian_name.setText(Followup[0].getGuardian_name());
                        tv_followups_done.setText(String.valueOf(Followup[0].getFollowups_done()));
                        followupsdone = Followup[0].getFollowups_done();
                        if (followupsdone > 2) {
                            btn_extend_followup.setVisibility(View.VISIBLE);
                        }
                        totalfollowups = Followup[0].getTotal_followups();
                        next_appointment = Followup[0].getNext_date();
//                        if (next_appointment != null)
//                        {
//                            btn_followup_done.setVisibility(View.VISIBLE);
//                        }
                        tv_child_name.setText(Followup[0].getChild_first_name() + " " + Followup[0].getChild_last_name());
                        tv_date_discharge.setText(String.valueOf(Followup[0].getDate_of_discharge()));

                        Calendar cal = GregorianCalendar.getInstance();
                        Date next_date = Followup[0].getNext_date();
                        cal.setTime(next_date);
                        cal.add(Calendar.DAY_OF_YEAR, -15);
                        Date last_screening = cal.getTime();
                        tv_lastscreening.setText(String.valueOf(last_screening));
                        referralid = Followup[0].getReferral_id();

                        Query query = referraldetails.whereEqualTo("referral_id", referralid);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        Referral ref = documentSnapshot.toObject(Referral.class);
                                        mref.add(ref);
                                        referraldocid = documentSnapshot.getId();
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
                    }
                }
            }
        });
    }

    private void Followupdone() {
        if(followupsdone < 3) {
            followupsdone = followupsdone + 1;
            db.collection("Followup").document(selectedchild).update(
                    "followups_done", followupsdone
            );
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, 15);
            final Date next_date = cal.getTime();
            db.collection("Followup").document(selectedchild).update(
                    "next_date", next_date
            ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), followupsdone + "th Followup Done and next appointment given for " + next_date, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), NRCActivity.class);
                    startActivity(intent);
                }
            });
        }
        else{
            followupsdone = followupsdone + 1;
            db.collection("Followup").document(selectedchild).update(
                    "followups_done", followupsdone
            );
            db.collection("Followup").document(selectedchild).update(
                    "next_date", FieldValue.delete()
            );
            Toast.makeText(getApplicationContext(), followupsdone + "th Followup Done", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), NRCActivity.class);
            startActivity(intent);
        }
    }

    private void extend_followup() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 15);
        final Date next_date = cal.getTime();
        db.collection("Followup").document(selectedchild).update(
                "next_date", next_date
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                followupsdone = followupsdone + 1;
                db.collection("Followup").document(selectedchild).update(
                        "followups_done", followupsdone
                );
                totalfollowups = totalfollowups + 1;
                db.collection("Followup").document(selectedchild).update("total_followups", totalfollowups);
                Toast.makeText(getApplicationContext(),followupsdone + "th followup done and Total followups for this child are now " + totalfollowups + ", next appointment on" + next_date, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), NRCActivity.class);
                startActivity(intent);
            }
        });
    }
    private void readmit() {
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
                    db1.collection("referral").document(referraldocid).update(
                            "status", "Admitted"
                    );
                    db1.collection("Followup").document(followupdocsnap)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("deleted followup ", followupdocsnap);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("error ", followupdocsnap + " ", e);
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