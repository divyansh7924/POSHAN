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

import com.example.sihtry1.models.Followup;
import com.example.sihtry1.models.Referral;
import com.example.sihtry1.models.RCR;
import com.example.sihtry1.models.Admits;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdmittedChildProfileActivity extends AppCompatActivity {

    private TextView tv_guardian_name, tv_child_name, tv_gender, tv_date_of_birth, tv_blood_group,
            tv_asha_tape, tv_height, tv_weight, tv_odema, tv_guardian_aadhaar, tv_child_aadhaar, tv_phone,
            tv_pin_code, tv_state, tv_aaganwadi, tv_symptoms, tv_village, tv_tehsil,
            tv_district, tv_date_admit, tv_admit_period, tv_anganwadi;
    private Button btn_discharge;
    private String selectedchild;
    private CollectionReference rcrref, admitdetails;
    FirebaseFirestore db;
    private ArrayList<RCR> mrcr = new ArrayList<>();
    String rcrselected, referralid;
    private ArrayList<Admits> admits = new ArrayList<>();
    String admitdocsnap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admitted_child_profile);
        Intent intent = getIntent();
        selectedchild = intent.getStringExtra("docid");

        tv_guardian_name = (TextView) findViewById(R.id.admitted_child_profile_tv_guardian_name);
        tv_child_name = (TextView) findViewById(R.id.admitted_child_profile_tv_child_name);
        tv_gender = (TextView) findViewById(R.id.admitted_child_profile_tv_child_gender);
        tv_date_of_birth = (TextView) findViewById(R.id.admitted_child_profile_tv_date_of_birth);
        tv_blood_group = (TextView) findViewById(R.id.admitted_child_profile_tv_blood_group);
        tv_asha_tape = (TextView) findViewById(R.id.admitted_child_profile_tv_asha_tape);
        tv_height = (TextView) findViewById(R.id.admitted_child_profile_tv_height);
        tv_weight = (TextView) findViewById(R.id.admitted_child_profile_tv_weight);
        tv_odema = (TextView) findViewById(R.id.admitted_child_profile_tv_odema);
        tv_guardian_aadhaar = (TextView) findViewById(R.id.admitted_child_profile_tv_guardian_aadhaar);
        tv_child_aadhaar = (TextView) findViewById(R.id.admitted_child_profile_tv_child_aadhaar);
        tv_phone = (TextView) findViewById(R.id.admitted_child_profile_tv_phone);
        tv_pin_code = (TextView) findViewById(R.id.admitted_child_profile_tv_pincode);
        tv_state = (TextView) findViewById(R.id.admitted_child_profile_tv_state);
        tv_aaganwadi = (TextView) findViewById(R.id.admitted_child_profile_tv_aaganwadi);
        tv_symptoms = (TextView) findViewById(R.id.admitted_child_profile_tv_symptom);
        tv_village = findViewById(R.id.admitted_child_profile_tv_village);
        tv_district = findViewById(R.id.admitted_child_profile_tv_district);
        tv_tehsil = findViewById(R.id.admitted_child_profile_tv_tehsil);
        tv_date_admit = findViewById(R.id.admitted_child_profile_tv_date_admit);
        btn_discharge = findViewById(R.id.admitted_child_profile_btn_discharge_child);
        tv_admit_period = findViewById(R.id.admitted_child_profile_tv_admit_period);
        tv_anganwadi = findViewById(R.id.admitted_child_profile_tv_anganwadi);


        db = FirebaseFirestore.getInstance();
        rcrref = db.collection("rcr");
        admitdetails = db.collection("Admit");
        final DocumentReference docRef = db.collection("referral").document(selectedchild);

        final Referral[] referral = {null};
        btn_discharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDischarge();
            }
        });

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    referral[0] = task.getResult().toObject(Referral.class);
                    if (referral[0] == null) {
                        Toast.makeText(getApplicationContext(), "null  ", Toast.LENGTH_SHORT).show();
                    } else {
                        tv_asha_tape.setText(String.valueOf(referral[0].getAsha_measure()));
                        tv_child_name.setText(referral[0].getChild_first_name());
                        tv_height.setText(String.valueOf(referral[0].getHeight()));
                        tv_weight.setText(String.valueOf(referral[0].getWeight()));
                        tv_phone.setText(referral[0].getPhone());
                        int oedema = referral[0].getOedema();
                        if (oedema == 0)
                            tv_odema.setText("0");
                        else if (oedema == 1)
                            tv_odema.setText("+");
                        else if (oedema == 2)
                            tv_odema.setText("++");
                        else
                            tv_odema.setText("+++");
                        tv_symptoms.setText(referral[0].getOther_symptoms());
                        tv_guardian_name.setText(referral[0].getGuadian_name());
                        tv_gender.setText(referral[0].getChild_gender());
                        tv_date_of_birth.setText(String.valueOf(referral[0].getDay_of_birth()));
                        tv_blood_group.setText(referral[0].getChild_gender());
                        tv_guardian_aadhaar.setText(String.valueOf(referral[0].getGuardian_aadhaar_num()));
                        tv_child_aadhaar.setText(String.valueOf(referral[0].getChild_aadhaar_num()));
                        tv_pin_code.setText(String.valueOf(referral[0].getPincode()));
                        tv_state.setText(referral[0].getState());
                        tv_village.setText(referral[0].getVillage());
                        tv_district.setText(referral[0].getDistrict());
                        tv_tehsil.setText(referral[0].getTehsil());

                        referralid = referral[0].getReferral_id();
                        rcrselected = referral[0].getRcr_id();

                        Query admitquery = admitdetails.whereEqualTo("referral_id", referralid);
                        admitquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        Admits admit = documentSnapshot.toObject(Admits.class);
                                        admits.add(admit);
                                        admitdocsnap = documentSnapshot.getId();
                                    }
                                    tv_date_admit.setText(String.valueOf(admits.get(0).getDate_of_admission()));
                                    tv_admit_period.setText(String.valueOf(admits.get(0).getDuration()) + " Weeks");
                                } else {
                                    Toast.makeText(getApplicationContext(), "RCR not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Query query = rcrref.whereEqualTo("user_id", rcrselected);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        RCR rcr = documentSnapshot.toObject(RCR.class);
                                        mrcr.add(rcr);
                                    }
                                    tv_anganwadi.setText(mrcr.get(0).getTitle());
                                } else {
                                    Toast.makeText(getApplicationContext(), "RCR not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
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
        followup.setNum_followups(3);
        followup.setReferral_id(referralid);

        newFollowup.set(followup).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseFirestore db1;
                    db1 = FirebaseFirestore.getInstance();
                    db1.collection("referral").document(selectedchild).update(
                            "status", "Discharged"
                    );
                    db1.collection("Admit").document(admitdocsnap)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("deleted admit document ", admitdocsnap);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("error ", admitdocsnap + " ", e);
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
}
