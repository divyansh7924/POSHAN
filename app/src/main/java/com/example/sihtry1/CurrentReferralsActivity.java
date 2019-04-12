package com.example.sihtry1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.sihtry1.adapters.CurrentReferralAdapter;
import com.example.sihtry1.models.Referral;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class CurrentReferralsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference childref = db.collection("referral");

    private CurrentReferralAdapter adapter;
    String profileselected;
    public Boolean ack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_referrals);

        setupRecyclerView();
        adapter.setOnItemClickListener(new CurrentReferralAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Referral refid = documentSnapshot.toObject(Referral.class);
                String id = documentSnapshot.getId();
                profileselected = documentSnapshot.getId();
                Toast.makeText(CurrentReferralsActivity.this,"position: " + position + "ID: " + id + profileselected, Toast.LENGTH_SHORT).show();
                db.collection("referral").document(profileselected).update(
                        "seen",1
                );
                showprofile();
            }
        });
    }

    public void setupRecyclerView() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = childref.whereEqualTo("nrc_id", userId).whereEqualTo("status","Referred").orderBy("child_first_name",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Referral> options = new FirestoreRecyclerOptions.Builder<Referral>()
                .setQuery(query, Referral.class)
                .build();
        adapter = new CurrentReferralAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerviewreferral);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    public void showprofile(){
        Intent intent = new Intent(this, ChildProfileActivity.class);
        intent.putExtra("docid", profileselected);
        startActivity(intent);
    }
}

