package com.example.sihtry1;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.example.sihtry1.adapters.StateAdapter;
import com.example.sihtry1.models.State;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StatesActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference stateref = db.collection("States");
    private StateAdapter adapter;
    String stateselected;
    FirestoreRecyclerOptions<State> options;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_states);

        Query query = stateref.orderBy("state", Query.Direction.ASCENDING);
        options = new FirestoreRecyclerOptions.Builder<State>()
                .setQuery(query, State.class)
                .build();
        recyclerView = findViewById(R.id.states_recyclerview);
        adapter = new StateAdapter(options, "");


        setupRecyclerView("");
    }

    public void setupRecyclerView(String queryStr) {
        adapter.updateData(queryStr);

        adapter.setOnItemClickListener(new StateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                State state = documentSnapshot.toObject(State.class);
                String id = documentSnapshot.getId();
                stateselected = state.getState();
                Toast.makeText(StatesActivity.this, "position: " + position + "ID: " + id + stateselected, Toast.LENGTH_SHORT).show();
                showNRCs();

            }
        });

        recyclerView.setAdapter(adapter);
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

    public void showNRCs() {
        Intent intent = new Intent(this, NrcListActivity.class);
        intent.putExtra("message", stateselected);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setQueryHint("Search State");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryStr) {
                setupRecyclerView(queryStr);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String queryStr) {
                setupRecyclerView(queryStr);
                return true;
            }
        });
        return true;
    }
}