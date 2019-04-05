package com.example.sihtry1;

import android.content.Intent;
import android.icu.util.IndianCalendar;
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
import com.google.firebase.firestore.ServerTimestamp;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AdmittedChildProfileActivity extends AppCompatActivity {

    private TextView tv_guardian_name, tv_child_name, tv_gender, tv_date_of_birth, tv_blood_group,
            tv_asha_tape, tv_height, tv_weight, tv_odema, tv_guardian_aadhaar, tv_child_aadhaar, tv_phone,
            tv_pin_code, tv_state, tv_symptoms, tv_village, tv_tehsil, tv_treated_for,
            tv_district, tv_date_admit, tv_admit_period, tv_anganwadi;
    private Button btn_discharge;
    private Referral selectedRef;
    private CollectionReference rcrref, admitdetails;
    FirebaseFirestore db;
    private ArrayList<RCR> mrcr = new ArrayList<>();
    String rcrselected, referralid, child_first_name, child_last_name, guardian_name, refDocSnap, admitDocSnap;
    private ArrayList<Admits> admits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admitted_child_profile);
        Intent intent = getIntent();
        selectedRef = (Referral) intent.getSerializableExtra("selectedRef");
        refDocSnap = getIntent().getStringExtra("refDocSnap");

        tv_guardian_name = findViewById(R.id.admitted_child_profile_tv_guardian_name);
        tv_child_name = findViewById(R.id.admitted_child_profile_tv_child_name);
        tv_gender = findViewById(R.id.admitted_child_profile_tv_child_gender);
        tv_date_of_birth = findViewById(R.id.admitted_child_profile_tv_date_of_birth);
        tv_blood_group = findViewById(R.id.admitted_child_profile_tv_blood_group);
        tv_asha_tape = findViewById(R.id.admitted_child_profile_tv_asha_tape);
        tv_height = findViewById(R.id.admitted_child_profile_tv_height);
        tv_weight = findViewById(R.id.admitted_child_profile_tv_weight);
        tv_odema = findViewById(R.id.admitted_child_profile_tv_odema);
        tv_guardian_aadhaar = findViewById(R.id.admitted_child_profile_tv_guardian_aadhaar);
        tv_child_aadhaar = findViewById(R.id.admitted_child_profile_tv_child_aadhaar);
        tv_phone = findViewById(R.id.admitted_child_profile_tv_phone);
        tv_pin_code = findViewById(R.id.admitted_child_profile_tv_pincode);
        tv_state = findViewById(R.id.admitted_child_profile_tv_state);
        tv_treated_for = findViewById(R.id.admitted_child_profile_tv_treated_for);
        tv_symptoms = findViewById(R.id.admitted_child_profile_tv_symptom);
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

        btn_discharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dischangeActivity();
            }
        });


        tv_asha_tape.setText(String.valueOf(selectedRef.getAsha_measure()));
        tv_child_name.setText(selectedRef.getChild_first_name());
        tv_height.setText(String.valueOf(selectedRef.getHeight()));
        tv_weight.setText(String.valueOf(selectedRef.getWeight()));
        tv_phone.setText(selectedRef.getPhone());
        int oedema = selectedRef.getOedema();
        if (oedema == 0)
            tv_odema.setText("0");
        else if (oedema == 1)
            tv_odema.setText("+");
        else if (oedema == 2)
            tv_odema.setText("++");
        else
            tv_odema.setText("+++");
        tv_symptoms.setText(selectedRef.getOther_symptoms());
        tv_guardian_name.setText(selectedRef.getGuadian_name());
        tv_gender.setText(selectedRef.getChild_gender());

        String dateOfBirth = String.valueOf(selectedRef.getDay_of_birth()) + ":" +
                String.valueOf(selectedRef.getMonth_of_birth()) + ":" +
                String.valueOf(selectedRef.getYear_of_birth());
        tv_date_of_birth.setText(dateOfBirth);

        tv_blood_group.setText(selectedRef.getBlood_group());
        tv_guardian_aadhaar.setText(String.valueOf(selectedRef.getGuardian_aadhaar_num()));
        tv_child_aadhaar.setText(String.valueOf(selectedRef.getChild_aadhaar_num()));
        tv_pin_code.setText(String.valueOf(selectedRef.getPincode()));
        tv_state.setText(selectedRef.getState());
        tv_village.setText(selectedRef.getVillage());
        tv_district.setText(selectedRef.getDistrict());
        tv_tehsil.setText(selectedRef.getTehsil());
        tv_treated_for.setText(selectedRef.getTreated_for());

        child_first_name = selectedRef.getChild_first_name();
        child_last_name = selectedRef.getChild_last_name();
        guardian_name = selectedRef.getGuadian_name();
        referralid = selectedRef.getReferral_id();
        rcrselected = selectedRef.getRcr_id();

        Query admitquery = admitdetails.whereEqualTo("referral_id", referralid);
        admitquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Admits admit = documentSnapshot.toObject(Admits.class);
                        admits.add(admit);
                        admitDocSnap = documentSnapshot.getId();
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

    private void dischangeActivity() {
        Intent intent = new Intent(this, DischangeInfoActivity.class);
        intent.putExtra("referral", selectedRef);
        intent.putExtra("refDocSnap", refDocSnap);
        intent.putExtra("admitDocSnap", admitDocSnap);
        startActivity(intent);
    }
}
