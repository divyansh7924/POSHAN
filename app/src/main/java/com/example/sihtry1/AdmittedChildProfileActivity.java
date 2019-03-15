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

    //    "ch stands for child"
    private TextView admitted_ch_guardian_name,admitted_ch_child_name,admitted_ch_gender,admitted_ch_date_of_admission,admitted_ch_admission_period,admitted_ch_date_of_birth,admitted_ch_blood_group,admitted_ch_asha_tape,admitted_ch_height,admitted_ch_weight,admitted_ch_odema,admitted_ch_guardian_aadhaar,admitted_ch_child_aadhaar,admitted_ch_phone,admitted_ch_address,admitted_ch_city,admitted_ch_pin_code,admitted_ch_state,admitted_ch_aaganwadi,admitted_ch_symptoms;
    private Button admitted_ch_discharge_child;
    private String selectedchild;
    FirebaseFirestore db;
    private CollectionReference rcrref,admitdetails;
    private ArrayList<RCR> mrcr = new ArrayList<>();
    private ArrayList<Admits> admits = new ArrayList<>();
    String rcrselected,referralid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admitted_child_profile);
        Intent intent = getIntent();
        selectedchild = intent.getStringExtra("docid");
        Toast.makeText(AdmittedChildProfileActivity.this,selectedchild,Toast.LENGTH_LONG).show();
        Log.v("llllll",selectedchild);

        admitted_ch_guardian_name = (TextView) findViewById(R.id.admitted_ch_guardian_name);
        admitted_ch_child_name = (TextView) findViewById(R.id.admitted_ch_child_name);
        admitted_ch_gender = (TextView) findViewById(R.id.admitted_ch_child_gender);
        admitted_ch_date_of_admission = (TextView) findViewById(R.id.admitted_ch_date_of_admission);
        admitted_ch_admission_period = (TextView) findViewById(R.id.admitted_ch_admission_period);
        admitted_ch_date_of_birth = (TextView) findViewById(R.id.admitted_ch_date_of_birth);
        admitted_ch_blood_group = (TextView) findViewById(R.id.admitted_ch_blood_group);
        admitted_ch_asha_tape = (TextView)findViewById(R.id.admitted_ch_asha_tape);
        admitted_ch_height = (TextView)findViewById(R.id.admitted_ch_height);
        admitted_ch_weight = (TextView)findViewById(R.id.admitted_ch_weight);
        admitted_ch_odema = (TextView)findViewById(R.id.admitted_ch_odema);
        admitted_ch_guardian_aadhaar = (TextView)findViewById(R.id.admitted_ch_guardian_aadhaar);
        admitted_ch_child_aadhaar = (TextView)findViewById(R.id.admitted_ch_child_aadhaar);
        admitted_ch_phone = (TextView)findViewById(R.id.admitted_ch_phone);
        admitted_ch_address = (TextView)findViewById(R.id.admitted_ch_address);
        admitted_ch_city = (TextView)findViewById(R.id.admitted_ch_city);
        admitted_ch_pin_code = (TextView)findViewById(R.id.admitted_ch_pincode);
        admitted_ch_state = (TextView)findViewById(R.id.admitted_ch_state);
        admitted_ch_aaganwadi = (TextView)findViewById(R.id.admitted_ch_aaganwadi);
        admitted_ch_symptoms = (TextView)findViewById(R.id.admitted_ch_symptom);
        admitted_ch_discharge_child = (Button) findViewById(R.id.admitted_ch_discharge_child);

        db = FirebaseFirestore.getInstance();
        rcrref = db.collection("rcr");
        admitdetails = db.collection("Admit");
        final DocumentReference docRef = db.collection("referral").document(selectedchild);

        final Referral[] referral = {null};
        admitted_ch_discharge_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("referral").document(selectedchild).update(
                        "status","Discharged"
                );
                createNewDischarge();
            }
        });

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    referral[0] = task.getResult().toObject(Referral.class);
                    if (referral[0] == null) {
//                        Toast.makeText(getApplicationContext(), "null  ", Toast.LENGTH_SHORT).show();
                    } else {
                        admitted_ch_asha_tape.setText(String.valueOf(referral[0].getAsha_measure()));
                        admitted_ch_child_name.setText(referral[0].getChild_first_name());
                        admitted_ch_height.setText(String.valueOf(referral[0].getHeight()));
                        admitted_ch_weight.setText(String.valueOf(referral[0].getWeight()));
                        admitted_ch_phone.setText(referral[0].getPhone());
                        int oedema = referral[0].getOedema();
                        if (oedema == 0)
                            admitted_ch_odema.setText("0");
                        else if(oedema == 1)
                            admitted_ch_odema.setText("+");
                        else if(oedema == 2)
                            admitted_ch_odema.setText("++");
                        else
                            admitted_ch_odema.setText("+++");
                        admitted_ch_symptoms.setText(referral[0].getOther_symptoms());
                        admitted_ch_guardian_name.setText(referral[0].getGuadian_name());
                        admitted_ch_gender.setText(referral[0].getChild_gender());
                        admitted_ch_date_of_birth.setText(String.valueOf(referral[0].getDay_of_birth()));
                        admitted_ch_blood_group.setText(referral[0].getChild_gender());
                        admitted_ch_guardian_aadhaar.setText(String.valueOf(referral[0].getGuardian_aadhaar_num()));
                        admitted_ch_child_aadhaar.setText(String.valueOf(referral[0].getChild_aadhaar_num()));
                        admitted_ch_address.setText(referral[0].getAddress());
                        admitted_ch_city.setText(referral[0].getCity());
                        admitted_ch_pin_code.setText(String.valueOf(referral[0].getPincode()));
                        admitted_ch_state.setText(referral[0].getState());
                        referralid = referral[0].getReferral_id();
                        rcrselected = referral[0].getRcr_id();
                        Query admitquery = admitdetails.whereEqualTo("referral_id",referralid);
                        admitquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        Admits admit = documentSnapshot.toObject(Admits.class);
                                        admits.add(admit);
                                    }
                                    admitted_ch_date_of_admission.setText(String.valueOf(admits.get(0).getDate_of_admission()));
                                    admitted_ch_admission_period.setText(String.valueOf(admits.get(0).getDuration())+" Weeks");
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "RCR not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Query query = rcrref.whereEqualTo("user_id",rcrselected);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        RCR rcr = documentSnapshot.toObject(RCR.class);
                                        mrcr.add(rcr);
                                    }
                                    admitted_ch_aaganwadi.setText(mrcr.get(0).getTitle());
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "RCR not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
    private void createNewDischarge(){
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
                    Toast.makeText(getApplicationContext(),"Discharged",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), NRCActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Discharge Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
