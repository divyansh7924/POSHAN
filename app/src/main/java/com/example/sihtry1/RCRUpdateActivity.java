package com.example.sihtry1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sihtry1.models.NRC;
import com.example.sihtry1.models.RCR;
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

public class RCRUpdateActivity extends AppCompatActivity {

    private Button btn_submit;
    private EditText et_phone;
    FirebaseFirestore db;
    CollectionReference rcrRef;
    String userId;
    RCR rcr = null;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcr_update);

        et_phone = findViewById(R.id.rcr_update_et_phone);
        btn_submit = findViewById(R.id.rcr_update_btn_submit);

        db = FirebaseFirestore.getInstance();
        rcrRef = db.collection("rcr");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = rcrRef.whereEqualTo("user_id", userId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        rcr = documentSnapshot.toObject(RCR.class);
                        docRef = documentSnapshot.getReference();
                    }

                    et_phone.setText(rcr.getPhone());
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
        docRef.update("phone", et_phone.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        RCRPanel();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Cannot Update", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void RCRPanel() {
        Intent intent = new Intent(this, RCRActivity.class);
        startActivity(intent);
        finish();
    }
}
