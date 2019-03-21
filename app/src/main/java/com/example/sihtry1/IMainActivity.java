
package com.example.sihtry1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.sihtry1.models.NRC;
import com.example.sihtry1.models.RCR;
import com.example.sihtry1.models.Referral;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IMainActivity {
    public void createNewNRC(final Context context, String user_id, int bed_count, int bed_vacant, String title,
                             String reg_certi, String reg_num, String address, String state, String city, int pincode, String phone, String email, final boolean verified, double lat, double lon) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference newNoteRef = db.collection("nrc").document();

        NRC nrc = new NRC(user_id, bed_count, email, bed_vacant, title, reg_certi, reg_num, address, state, city, pincode, phone, verified, lat, lon);

        newNoteRef.set(nrc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    verificationDue(context);

                } else {
                    Toast.makeText(context.getApplicationContext(), "Registeration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createNewRCR(final Context context, String user_id, String title,
                             String reg_certi, String reg_num, String address, String state, String city, int pincode, String phone, String email, final boolean verified, double lat, double lon) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference newNoteRef = db.collection("rcr").document();

        RCR rcr = new RCR(user_id, reg_certi, title, address, state, reg_num, city, pincode, phone, email, verified, lat, lon);

        newNoteRef.set(rcr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    verificationDue(context);

                } else {
                    Toast.makeText(context.getApplicationContext(), "Registeration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void verificationDue(Context context) {
        Intent intent = new Intent(context, VerificationDueActivity.class);
        context.startActivity(intent);
    }

    public void rcrUI(Context context) {
        Intent intent = new Intent(context, RCRActivity.class);
        context.startActivity(intent);
    }

    public ArrayList<NRC> getNRCs() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference nrcCollecRef = db.collection("nrc");

        Query nrcQuery = nrcCollecRef.whereEqualTo("city", "delhinew");

        final ArrayList<NRC> nrcList = new ArrayList<>();
        nrcCollecRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Log.v("IMainActivity", "hello");
                        nrcList.add(documentSnapshot.toObject(NRC.class));
                    }
                } else {
                    Log.v("IMainAcitivity", "Task Not Completed");
                }
            }
        });

        return nrcList;
    }
}