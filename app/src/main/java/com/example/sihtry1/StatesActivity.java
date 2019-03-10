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

public class StatesActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference stateref = db.collection("States");

    private StateAdapter adapter;
    String stateselected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_states);

        setupRecyclerView();
        adapter.setOnItemClickListener(new StateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                NRC state = documentSnapshot.toObject(NRC.class);
                String id = documentSnapshot.getId();
                stateselected = state.getState();
                Toast.makeText(StatesActivity.this,"position: " + position + "ID: " + id + stateselected, Toast.LENGTH_SHORT).show();
                showcities();

            }
        });

    }

    public void setupRecyclerView() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Query query = stateref.orderBy("state", Query.Direction.ASCENDING);



        FirestoreRecyclerOptions<RCR> options = new FirestoreRecyclerOptions.Builder<RCR>()
                .setQuery(query, RCR.class)
                .build();



        adapter = new StateAdapter(options);


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
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
    public void showcities(){
        Intent intent = new Intent(this, NrcListActivity.class);
        intent.putExtra("message", stateselected);
        startActivity(intent);
    }
}