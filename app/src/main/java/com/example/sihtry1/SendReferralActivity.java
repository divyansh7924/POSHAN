package com.example.sihtry1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.sihtry1.models.Referral;
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

public class SendReferralActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference childref;
    private SendReferralAdapter adapter;
    private String selectedNrcId;
    private ArrayList<NRC> mNrc = new ArrayList<>();
    private TextView tv_nrc_title, tv_nrc_statepin, tv_nrc_addresscity;
    private AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_referral);

        tv_nrc_addresscity = (TextView) findViewById(R.id.send_referral_tv_nrc_addresscity);
        tv_nrc_statepin = (TextView) findViewById(R.id.send_referral_tv_nrc_statepin);
        tv_nrc_title = (TextView) findViewById(R.id.send_referral_tv_nrc_title);

        alertBuilder = new AlertDialog.Builder(this);

        db = FirebaseFirestore.getInstance();
        childref = db.collection("referral");
        CollectionReference nrcRef = db.collection("nrc");

        setupRecyclerView();
        Intent intent = getIntent();
        selectedNrcId = intent.getStringExtra("NRC_ID");

        Query query = nrcRef.whereEqualTo("user_id", selectedNrcId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        NRC nrc = documentSnapshot.toObject(NRC.class);
                        mNrc.add(nrc);
                    }

                    tv_nrc_title.setText(mNrc.get(0).getTitle());
                    tv_nrc_addresscity.setText(mNrc.get(0).getAddress() + ", " + mNrc.get(0).getCity());
                    tv_nrc_statepin.setText(mNrc.get(0).getState() + ", " + mNrc.get(0).getPincode());
                } else {
                    Toast.makeText(getApplicationContext(), "NRC not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapter.setOnItemClickListener(new SendReferralAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {

                alertBuilder.setMessage("Sent referral to this NRC?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendReferral(documentSnapshot);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = alertBuilder.create();
                alert.setTitle("Send Referral");
                alert.show();

            }
        });
    }

    private void sendReferral(DocumentSnapshot documentSnapshot) {
        Referral referral = documentSnapshot.toObject(Referral.class);
        referral.setNrc_id(selectedNrcId);
        referral.setStatus("Referred");
        referral.setSeen(0);
        submit(referral);
        documentSnapshot.getReference().delete();
    }


    private void submit(Referral referral) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newReferralRef = db.collection("referral").document();
        newReferralRef.set(referral).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), RCRActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Registeration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setupRecyclerView() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toast.makeText(SendReferralActivity.this, "userid" + userId, Toast.LENGTH_SHORT).show();

        Query query = childref.whereEqualTo("rcr_id", userId).whereEqualTo("nrc_id", null);

        FirestoreRecyclerOptions<Referral> options = new FirestoreRecyclerOptions.Builder<Referral>()
                .setQuery(query, Referral.class)
                .build();

        adapter = new SendReferralAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerviewsendreferral);
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