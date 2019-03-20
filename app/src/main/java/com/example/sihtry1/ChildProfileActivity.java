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

import com.example.sihtry1.models.NRC;
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

public class ChildProfileActivity extends AppCompatActivity {

    private TextView tv_guardian_name, tv_child_name, tv_gender, tv_date_of_birth, tv_blood_group,
            tv_asha_tape, tv_height, tv_weight, tv_odema, tv_guardian_aadhaar, tv_child_aadhaar, tv_phone,
            tv_pin_code, tv_state, tv_aaganwadi, tv_symptoms, tv_village, tv_tehsil, tv_treated_for,
            tv_district, tv_date_screened;
    private EditText et_admission_period;
    private Button btn_admit_child;
    private String selectedchild;
    FirebaseFirestore db;
    private CollectionReference rcrref;
    private ArrayList<RCR> mrcr = new ArrayList<>();
    String rcrselected, referralid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);
        Intent intent = getIntent();
        selectedchild = intent.getStringExtra("docid");

        tv_guardian_name = (TextView) findViewById(R.id.child_profile_tv_guardian_name);
        tv_child_name = (TextView) findViewById(R.id.child_profile_tv_child_name);
        tv_gender = (TextView) findViewById(R.id.child_profile_tv_child_gender);
        tv_date_of_birth = (TextView) findViewById(R.id.child_profile_tv_date_of_birth);
        tv_blood_group = (TextView) findViewById(R.id.child_profile_tv_blood_group);
        tv_treated_for = findViewById(R.id.child_profile_tv_treated_for);
        tv_asha_tape = (TextView) findViewById(R.id.child_profile_tv_asha_tape);
        tv_height = (TextView) findViewById(R.id.child_profile_tv_height);
        tv_weight = (TextView) findViewById(R.id.child_profile_tv_weight);
        tv_odema = (TextView) findViewById(R.id.child_profile_tv_odema);
        tv_guardian_aadhaar = (TextView) findViewById(R.id.child_profile_tv_guardian_aadhaar);
        tv_child_aadhaar = (TextView) findViewById(R.id.child_profile_tv_child_aadhaar);
        tv_phone = (TextView) findViewById(R.id.child_profile_tv_phone);
        tv_pin_code = (TextView) findViewById(R.id.child_profile_tv_pincode);
        tv_state = (TextView) findViewById(R.id.child_profile_tv_state);
        tv_aaganwadi = (TextView) findViewById(R.id.child_profile_tv_aaganwadi);
        tv_symptoms = (TextView) findViewById(R.id.child_profile_tv_symptom);
        et_admission_period = (EditText) findViewById(R.id.child_profile_et_admission_period);
        btn_admit_child = (Button) findViewById(R.id.child_profile_btn_admit_child);
        tv_village = findViewById(R.id.child_profile_tv_village);
        tv_district = findViewById(R.id.child_profile_tv_district);
        tv_tehsil = findViewById(R.id.child_profile_tv_tehsil);
        tv_date_screened = findViewById(R.id.child_profile_tv_date_screened);

        db = FirebaseFirestore.getInstance();
        rcrref = db.collection("rcr");
        final DocumentReference docRef = db.collection("referral").document(selectedchild);

        final Referral[] referral = {null};
        btn_admit_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAdmission();
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

                        String dateOfBirth = String.valueOf(referral[0].getDay_of_birth()) + ":" +
                                String.valueOf(referral[0].getMonth_of_birth()) + ":" +
                                String.valueOf(referral[0].getYear_of_birth());
                        tv_date_of_birth.setText(dateOfBirth);

                        tv_blood_group.setText(referral[0].getBlood_group());
                        tv_guardian_aadhaar.setText(String.valueOf(referral[0].getGuardian_aadhaar_num()));
                        tv_child_aadhaar.setText(String.valueOf(referral[0].getChild_aadhaar_num()));
                        tv_pin_code.setText(String.valueOf(referral[0].getPincode()));
                        tv_state.setText(referral[0].getState());
                        tv_village.setText(referral[0].getVillage());
                        tv_district.setText(referral[0].getDistrict());
                        tv_tehsil.setText(referral[0].getTehsil());
                        tv_date_screened.setText(referral[0].getDate_screened().toString());
                        tv_treated_for.setText(referral[0].getTreated_for());


                        referralid = referral[0].getReferral_id();
                        rcrselected = referral[0].getRcr_id();

                        Query query = rcrref.whereEqualTo("user_id", rcrselected);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        RCR rcr = documentSnapshot.toObject(RCR.class);
                                        mrcr.add(rcr);
                                    }
                                    tv_aaganwadi.setText(mrcr.get(0).getTitle());
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

    private void createNewAdmission() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newAdmit = db.collection("Admit").document();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        int duration = Integer.parseInt(String.valueOf(et_admission_period.getText()));
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
                    db1.collection("referral").document(selectedchild).update("status", "Admitted");
                    Toast.makeText(getApplicationContext(), "Admitted", Toast.LENGTH_SHORT).show();
                    bedOccupied();
                    Intent intent = new Intent(getApplicationContext(), NRCActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Admission Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void bedOccupied() {
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

                        documentReference[0].update("bed_vacant", mNrc.get(0).getBed_vacant() - 1)
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
}
