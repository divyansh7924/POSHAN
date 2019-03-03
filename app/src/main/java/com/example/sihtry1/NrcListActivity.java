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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference nrcref = db.collection("nrc");
    String str;

    private NrcListAdapter adapter;
//    String stateselected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrc_list);

        Intent intent = getIntent();
        str = intent.getStringExtra("message");
        Toast.makeText(NrcListActivity.this, "str" + str, Toast.LENGTH_LONG).show();

        setupRecyclerView();

        adapter.setOnItemClickListener(new NrcListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                NRC Title = documentSnapshot.toObject(NRC.class);
                String id = documentSnapshot.getId();
                str = Title.getUser_id();
                Toast.makeText(NrcListActivity.this,"position: " + position + "ID: " + id + str, Toast.LENGTH_SHORT).show();
                showsendref();

            }
        });


    }

    public void setupRecyclerView() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Query query = nrcref.whereEqualTo("city", str).orderBy("title",Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<RCR> options = new FirestoreRecyclerOptions.Builder<RCR>()
                .setQuery(query, RCR.class)
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
    public void showsendref(){
        Intent intent = new Intent(this, SendReferralActivity.class);
        intent.putExtra("message", str);
        startActivity(intent);
    }
}

