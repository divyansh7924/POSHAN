package com.example.sihtry1;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sihtry1.models.NRC;
import com.example.sihtry1.models.RCR;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class NrcListActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference nrcref;
    private NrcListAdapter adapter;
    final ArrayList<NRC> nrcArrayList = new ArrayList<>();
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrc_list);

        db = FirebaseFirestore.getInstance();
        nrcref = db.collection("nrc");

        getCurrentLocation();
    }


    public void getCurrentLocation() {
        db = FirebaseFirestore.getInstance();
        CollectionReference rcrRef = db.collection("rcr");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = rcrRef.whereEqualTo("user_id", userId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    RCR rcr = null;
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        rcr = documentSnapshot.toObject(RCR.class);
                        DocumentReference docRef = documentSnapshot.getReference();
                    }
                    double lat = rcr.getLat();
                    double lon = rcr.getLon();

                    getNRCDistance(lat, lon);
                } else {

                }
            }
        });
    }

    private void getNRCDistance(double lat, double lon) {
        final Location currentLocation = new Location("");
        currentLocation.setLatitude(lat);
        currentLocation.setLongitude(lon);
        final Location nrcLocation = new Location("");

        Query query = nrcref.whereEqualTo("verified", true);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        NRC nrc = documentSnapshot.toObject(NRC.class);
                        nrcArrayList.add(nrc);
                    }

                    for (int i = 0; i < nrcArrayList.size(); i++) {
                        nrcLocation.setLongitude(nrcArrayList.get(i).getLon());
                        nrcLocation.setLatitude(nrcArrayList.get(i).getLat());
                        nrcArrayList.get(i).setDistance(currentLocation.distanceTo(nrcLocation) / 1000);
                        Log.v("NrcListActivity", nrcArrayList.get(i).getLat() + " " + nrcArrayList.get(i).getLon());
                    }

                    Collections.sort(nrcArrayList, new Comparator<NRC>() {
                        @Override
                        public int compare(NRC o1, NRC o2) {
                            return Double.compare(o1.getDistance(), o2.getDistance());
                        }
                    });

                    for (int i = 0; i < nrcArrayList.size(); i++) {
                        Log.v("NrcListActivity", nrcArrayList.get(i).getLat() + " " + nrcArrayList.get(i).getLon());
                    }

                    setUpRecyclerView();
                } else {
                    Toast.makeText(getApplicationContext(), "NRC not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerviewnrc);
        NrcListAdapter2 nrcListAdapter2 = new NrcListAdapter2(this, nrcArrayList);
        recyclerView.setAdapter(nrcListAdapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
    }

}

