package com.example.sihtry1;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NrcListActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference nrcref;

    private NrcListAdapter adapter;
    String stateselected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrc_list);

        db = FirebaseFirestore.getInstance();
        nrcref = db.collection("nrc");

        Intent intent = getIntent();
        stateselected = intent.getStringExtra("message");
        Toast.makeText(NrcListActivity.this, "str " + stateselected, Toast.LENGTH_LONG).show();

        setupRecyclerView();

        adapter.setOnItemClickListener(new NrcListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.toObject(NRC.class).getUser_id();
                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();

                sendref(id);
            }
        });


    }

    public void setupRecyclerView() {
        Query query = nrcref.whereEqualTo("state", stateselected);

        FirestoreRecyclerOptions<NRC> options = new FirestoreRecyclerOptions.Builder<NRC>()
                .setQuery(query, NRC.class)
                .build();

        adapter = new NrcListAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerviewnrc);
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

    public void sendref(String id) {
        Intent intent = new Intent(this, SendReferralActivity.class);
        intent.putExtra("NRC_ID", id);
        startActivity(intent);
    }
}

