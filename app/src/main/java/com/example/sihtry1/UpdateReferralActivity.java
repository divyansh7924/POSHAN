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
import android.widget.TextView;
import android.widget.Toast;

import com.example.sihtry1.models.Referral;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateReferralActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText et_child_first_name, et_child_last_name, et_child_symptoms, et_asha_measure, et_child_height, et_child_weight, et_phone;
    private Spinner oedema_spinner;
    private Button btn_submit;
    private String pathToDoc;

    int oedema_stage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_referral);

        pathToDoc = getIntent().getStringExtra("docRef");

        et_asha_measure = (EditText) findViewById(R.id.update_referral_et_ashamsmt);
        et_child_first_name = (EditText) findViewById(R.id.update_referral_et_child_f_name);
        et_child_last_name = (EditText) findViewById(R.id.update_referral_et_child_l_name);
        et_child_height = (EditText) findViewById(R.id.update_referral_et_height);
        et_child_weight = (EditText) findViewById(R.id.update_referral_et_weight);
        et_phone = (EditText) findViewById(R.id.update_referral_et_phone);
        btn_submit = (Button) findViewById(R.id.update_referral_btn_submit);
        oedema_spinner = (Spinner) findViewById(R.id.update_referral_spinner_oedema);
        et_child_symptoms = (EditText) findViewById(R.id.update_referral_et_symptoms);

        oedema_spinner.setOnItemSelectedListener(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.document(pathToDoc);

        Toast.makeText(getApplicationContext(), docRef.getId(), Toast.LENGTH_SHORT).show();

        final Referral[] referral = {null};

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    referral[0] = task.getResult().toObject(Referral.class);
                    if (referral[0] == null) {
                        Toast.makeText(getApplicationContext(), "is null bro", Toast.LENGTH_SHORT).show();
                    } else {
                        et_asha_measure.setText(String.valueOf(referral[0].getAsha_measure()));
                        et_child_first_name.setText(referral[0].getChild_first_name());
                        et_child_last_name.setText(referral[0].getChild_last_name());
                        et_child_height.setText(String.valueOf(referral[0].getHeight()));
                        et_child_weight.setText(String.valueOf(referral[0].getWeight()));
                        et_phone.setText(referral[0].getPhone());
                        oedema_spinner.setSelection(referral[0].getOedema() + 1);
                        et_child_symptoms.setText(referral[0].getOther_symptoms());
                    }
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(referral[0]);
                docRef.delete();
            }
        });

    }

    private void submit(Referral referral) {
        referral.setChild_first_name(et_child_first_name.getText().toString());
        referral.setChild_last_name(et_child_last_name.getText().toString());
        referral.setAsha_measure(Float.parseFloat(et_asha_measure.getText().toString()));
        referral.setHeight(Float.parseFloat(et_child_height.getText().toString()));
        referral.setWeight(Float.parseFloat(et_child_weight.getText().toString()));
        referral.setPhone(et_phone.getText().toString());
        referral.setOther_symptoms(et_child_symptoms.getText().toString());
        referral.setOedema(oedema_stage);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newReferralRef = db.collection("referral").document();
        newReferralRef.set(referral).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), RCRActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "Registeration Failed", Toast.LENGTH_SHORT).show();
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
