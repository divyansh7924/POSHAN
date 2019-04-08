package com.example.sihtry1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sihtry1.models.Followup;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class FollowupsListActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference childref = db.collection("Followup");
    private FollowUpsAdapter adapter;
    private String followupDocId;
    private Followup followup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followups_list);

        setupRecyclerView();
        adapter.setOnItemClickListener(new FollowUpsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                followupDocId = documentSnapshot.getId();
                followup = documentSnapshot.toObject(Followup.class);
                showprofile();
            }
        });
    }

    public void setupRecyclerView() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = childref.whereEqualTo("nrc_id", userId).orderBy("next_date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Followup> options = new FirestoreRecyclerOptions.Builder<Followup>()
                .setQuery(query, Followup.class)
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

    public void showprofile() {
        Intent intent = new Intent(this, FollowupChildActivity.class);
        intent.putExtra("followupDocId", followupDocId);
        intent.putExtra("followupObject", followup);
        startActivity(intent);
    }
}

