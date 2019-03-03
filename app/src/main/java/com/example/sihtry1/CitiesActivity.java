package com.example.sihtry1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.sihtry1.models.NRC;
import com.example.sihtry1.models.RCR;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CitiesActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cityref = db.collection("nrc");

    private CityAdapter adapter;
    String str;
    String selectedcity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
//        Bundle bundle = getIntent().getExtras();
//        String message = bundle.getString("message");
        Intent intent = getIntent();
        str = intent.getStringExtra("message");
        Toast.makeText(CitiesActivity.this, "str" + str, Toast.LENGTH_LONG).show();
        setupRecyclerView();
        adapter.setOnItemClickListener(new CityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                NRC city = documentSnapshot.toObject(NRC.class);
                String id = documentSnapshot.getId();
                selectedcity = city.getCity();
                Toast.makeText(CitiesActivity.this,"position: " + position + "ID: " + id + selectedcity, Toast.LENGTH_SHORT).show();
                shownrcs();

            }
        });

    }

    public void setupRecyclerView() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        //Query query1 = cityref.orderBy("city", Query.Direction.ASCENDING);
        Query query = cityref.whereEqualTo("state", str).orderBy("city",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<RCR> options = new FirestoreRecyclerOptions.Builder<RCR>()
                .setQuery(query, RCR.class)
                .build();


        adapter = new CityAdapter(options);


        RecyclerView recyclerView = findViewById(R.id.recyclerviewcity);
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
    public void shownrcs(){
        Intent intent = new Intent(this, NrcListActivity.class);
        intent.putExtra("message", selectedcity);
        startActivity(intent);
    }
}
