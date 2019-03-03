package com.example.sihtry1;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.sihtry1.models.NRC;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VerificationDueActivity extends AppCompatActivity {
    private TextView verify_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_due);

        verify_tv = (TextView) findViewById(R.id.verify_tv);

//        change_tv();
    }

//    public void change_tv() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference nrcCollecRef = db.collection("nrc");
//
//        Query nrcQuery = nrcCollecRef.whereEqualTo("city", "delhinew");
//
//        final ArrayList<NRC> nrcList = new ArrayList<>();
//        nrcCollecRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                        Log.v("IMainActivity", "hello");
//                        nrcList.add(documentSnapshot.toObject(NRC.class));
//                    }
//
//                    for (int i = 0; i < nrcList.size(); i++) {
//                        verify_tv.append("\n" + nrcList.get(i).getUser_id());
//                    }
//                } else {
//                    Log.v("IMainAcitivity", "Task Not Completed");
//                }
//            }
//        });
//
//        Log.v("VerificationDueActivity", String.valueOf(nrcList.size()));
//    }
}
