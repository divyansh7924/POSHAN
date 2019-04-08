package com.example.sihtry1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Locale;

public class SendReferralActivity extends AppCompatActivity {

    private static final String TAG = "SendReferralActivity";

    private FirebaseFirestore db;
    private CollectionReference childref;
    private SendReferralAdapter adapter;
    private String selectedNrcId;
    private ArrayList<NRC> mNrc = new ArrayList<>();
    private TextView tv_nrc_title, tv_nrc_statepin, tv_nrc_addresscity, tv_bed_avl;
    private Button btn_locate;
    private AlertDialog.Builder alertBuilder;
    private NRC nrc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_referral);

        tv_nrc_addresscity = findViewById(R.id.send_referral_tv_nrc_addresscity);
        tv_nrc_statepin = findViewById(R.id.send_referral_tv_nrc_statepin);
        tv_nrc_title = findViewById(R.id.send_referral_tv_nrc_title);
        tv_bed_avl = findViewById(R.id.send_referral_tv_bed_avl);
        btn_locate = findViewById(R.id.send_referral_btn_locate);

        alertBuilder = new AlertDialog.Builder(this);

        db = FirebaseFirestore.getInstance();
        childref = db.collection("referral");
        CollectionReference nrcRef = db.collection("nrc");

        setupRecyclerView();
        Intent intent = getIntent();
        selectedNrcId = intent.getStringExtra("NRC_ID");
        nrc = (NRC) intent.getSerializableExtra("NRCobj");

        tv_nrc_title.setText(nrc.getTitle());
        tv_nrc_addresscity.setText(nrc.getAddress() + ", " + nrc.getCity());
        tv_nrc_statepin.setText(nrc.getState() + ", " + nrc.getPincode());
        tv_bed_avl.setText("Bed Availability: " + nrc.getBed_vacant() + " / " + nrc.getBed_count());

        btn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = nrc.getLat();
                double longitude = nrc.getLon();
                String label = nrc.getTitle();
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
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