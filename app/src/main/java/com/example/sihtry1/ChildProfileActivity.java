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

public class ChildProfileActivity extends AppCompatActivity {

//    "ch stands for child"
    private TextView ch_guardian_name,ch_child_name,ch_gender,ch_date_of_birth,ch_blood_group,ch_asha_tape,ch_height,ch_weight,ch_odema,ch_guardian_aadhaar,ch_child_aadhaar,ch_phone,ch_address,ch_city,ch_pin_code,ch_state,ch_aaganwadi,ch_symptoms;
    private EditText ch_admission_period;
    private Button ch_admit_child;
    private String selectedchild;
    FirebaseFirestore db;
    private CollectionReference rcrref;
    private ArrayList<RCR> mrcr = new ArrayList<>();
    String rcrselected,referralid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);
        Intent intent = getIntent();
        selectedchild = intent.getStringExtra("docid");
        Toast.makeText(ChildProfileActivity.this,selectedchild,Toast.LENGTH_LONG).show();
        Log.v("llllll",selectedchild);

        ch_guardian_name = (TextView) findViewById(R.id.ch_guardian_name);
        ch_child_name = (TextView) findViewById(R.id.ch_child_name);
        ch_gender = (TextView) findViewById(R.id.ch_child_gender);
        ch_date_of_birth = (TextView) findViewById(R.id.ch_date_of_birth);
        ch_blood_group = (TextView) findViewById(R.id.ch_blood_group);
        ch_asha_tape = (TextView)findViewById(R.id.ch_asha_tape);
        ch_height = (TextView)findViewById(R.id.ch_height);
        ch_weight = (TextView)findViewById(R.id.ch_weight);
        ch_odema = (TextView)findViewById(R.id.ch_odema);
        ch_guardian_aadhaar = (TextView)findViewById(R.id.ch_guardian_aadhaar);
        ch_child_aadhaar = (TextView)findViewById(R.id.ch_child_aadhaar);
        ch_phone = (TextView)findViewById(R.id.ch_phone);
        ch_address = (TextView)findViewById(R.id.ch_address);
        ch_city = (TextView)findViewById(R.id.ch_city);
        ch_pin_code = (TextView)findViewById(R.id.ch_pincode);
        ch_state = (TextView)findViewById(R.id.ch_state);
        ch_aaganwadi = (TextView)findViewById(R.id.ch_aaganwadi);
        ch_symptoms = (TextView)findViewById(R.id.ch_symptom);
        ch_admission_period = (EditText) findViewById(R.id.ch_admission_period);
        ch_admit_child = (Button) findViewById(R.id.ch_admit_child);

        db = FirebaseFirestore.getInstance();
        rcrref = db.collection("rcr");
        final DocumentReference docRef = db.collection("referral").document(selectedchild);

        final Referral[] referral = {null};
        ch_admit_child.setOnClickListener(new View.OnClickListener() {
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
//                        Toast.makeText(getApplicationContext(), "null  ", Toast.LENGTH_SHORT).show();
                    } else {
                        ch_asha_tape.setText(String.valueOf(referral[0].getAsha_measure()));
                        ch_child_name.setText(referral[0].getChild_first_name());
                        ch_height.setText(String.valueOf(referral[0].getHeight()));
                        ch_weight.setText(String.valueOf(referral[0].getWeight()));
                        ch_phone.setText(referral[0].getPhone());
                        int oedema = referral[0].getOedema();
                        if (oedema == 0)
                            ch_odema.setText("0");
                        else if(oedema == 1)
                            ch_odema.setText("+");
                        else if(oedema == 2)
                            ch_odema.setText("++");
                        else
                            ch_odema.setText("+++");
                        ch_symptoms.setText(referral[0].getOther_symptoms());
                        ch_guardian_name.setText(referral[0].getGuadian_name());
                        ch_gender.setText(referral[0].getChild_gender());
                        ch_date_of_birth.setText(String.valueOf(referral[0].getDay_of_birth()));
                        ch_blood_group.setText(referral[0].getChild_gender());
                        ch_guardian_aadhaar.setText(String.valueOf(referral[0].getGuardian_aadhaar_num()));
                        ch_child_aadhaar.setText(String.valueOf(referral[0].getChild_aadhaar_num()));
                        ch_address.setText(referral[0].getAddress());
                        ch_city.setText(referral[0].getCity());
                        ch_pin_code.setText(String.valueOf(referral[0].getPincode()));
                        ch_state.setText(referral[0].getState());
                        referralid = referral[0].getReferral_id();
                        rcrselected = referral[0].getRcr_id();
                        Query query = rcrref.whereEqualTo("user_id",rcrselected);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        RCR rcr = documentSnapshot.toObject(RCR.class);
                                        mrcr.add(rcr);
                                    }
                                    ch_aaganwadi.setText(mrcr.get(0).getTitle());
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
    private void createNewAdmission(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newAdmit = db.collection("Admit").document();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        int duration= Integer.parseInt(String.valueOf(ch_admission_period.getText()));
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
                    db1.collection("referral").document(selectedchild).update(
                            "status","Admitted"
                    );
                    Toast.makeText(getApplicationContext(),"Admitted",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), NRCActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Admission Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
