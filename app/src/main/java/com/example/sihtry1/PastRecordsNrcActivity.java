package com.example.sihtry1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sihtry1.adapters.PastRecordsNrcAdapter;
import com.example.sihtry1.models.PastRecord;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class PastRecordsNrcActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference pastRecordRef = db.collection("PastRecord");

    private PastRecordsNrcAdapter adapter;
    private PastRecord pastRecord = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_records_nrc);

        setupRecyclerView();
        adapter.setOnItemClickListener(new PastRecordsNrcAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                pastRecord = documentSnapshot.toObject(PastRecord.class);
                Intent intent = new Intent(PastRecordsNrcActivity.this, PastRecordInfoActivity.class);
                intent.putExtra("PastRecord", pastRecord);
                startActivity(intent);
            }
        });
    }

    public void setupRecyclerView() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = pastRecordRef.whereEqualTo("nrc_id", userId);
        FirestoreRecyclerOptions<PastRecord> options = new FirestoreRecyclerOptions.Builder<PastRecord>()
                .setQuery(query, PastRecord.class)
                .build();

        adapter = new PastRecordsNrcAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerviewnrcpastrecords);
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

}

