package com.example.sihtry1;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sihtry1.models.NRC;
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

public class NRCUpdateActivity extends AppCompatActivity {

    private EditText et_phone, et_bedVacant, et_totelBed;
    private Button btn_submit;
    FirebaseFirestore db;
    CollectionReference nrcRef;
    String userId;
    NRC nrc = null;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrc_update);

        et_phone = findViewById(R.id.nrc_update_et_phone);
        btn_submit = findViewById(R.id.nrc_update_btn_submit);
        et_bedVacant = findViewById(R.id.nrc_update_et_bed_vacant);
        et_totelBed = findViewById(R.id.nrc_update_et_bed_count);

        db = FirebaseFirestore.getInstance();
        nrcRef = db.collection("nrc");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = nrcRef.whereEqualTo("user_id", userId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        nrc = documentSnapshot.toObject(NRC.class);
                        docRef = documentSnapshot.getReference();
                    }

                    et_phone.setText(nrc.getPhone());
                    et_bedVacant.setText(String.valueOf(nrc.getBed_vacant()));
                    et_totelBed.setText(String.valueOf(nrc.getBed_count()));
                } else {
                    Toast.makeText(getApplicationContext(), "NRC not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        docRef.update("phone", et_phone.getText().toString(), "bed_count", Integer.parseInt(et_totelBed.getText().toString()),
                "bed_vacant", Integer.parseInt(et_bedVacant.getText().toString()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Cannot Update", Toast.LENGTH_SHORT).show();
                            }
                        });
    }
}
