package com.example.sihtry1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sihtry1.models.Referral;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class FollowupsListActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference childref = db.collection("referral");

    private FollowUpsAdapter adapter;
    String profileselected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followups_list);

        setupRecyclerView();
        adapter.setOnItemClickListener(new FollowUpsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Referral refid = documentSnapshot.toObject(Referral.class);
                String id = documentSnapshot.getId();
                profileselected = documentSnapshot.getId();
//                showprofile();
            }
        });
    }

    public void setupRecyclerView() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = childref.whereEqualTo("nrc_id", userId).whereEqualTo("status","Discharged").orderBy("child_first_name",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Referral> options = new FirestoreRecyclerOptions.Builder<Referral>()
                .setQuery(query, Referral.class)
                .build();

        adapter = new FollowUpsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerviewfollowups);
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
        Intent intent = new Intent(this, AdmittedChildProfileActivity.class);
        intent.putExtra("docid", profileselected);
        startActivity(intent);
    }
}

