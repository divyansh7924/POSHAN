package com.example.sihtry1;

import android.R.layout;
import android.accounts.AccountManagerFuture;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NRCRegisterActivity extends AppCompatActivity {

    private Button submit;
    public FirebaseFirestore db;
    private TextView tv_reg_doc_name;
    private EditText et_bed_count, et_bed_vacant, et_title, et_address, et_city, et_pincode, et_phone, et_reg_num;
    private Spinner sp_state;
    private ArrayList<String> states = new ArrayList<>();
    private Button btn_browse;
    private StorageReference mStorageRef;
    private static final int PICK_PDF_REQUEST = 234;
    private Uri filepath;
    private Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrc_register);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        submit = (Button) findViewById(R.id.nrc_reg_submit);
        et_bed_count = (EditText) findViewById(R.id.nrc_reg_et_bed_count);
        et_bed_vacant = (EditText) findViewById(R.id.nrc_reg_et_bed_vacant);
        et_title = (EditText) findViewById(R.id.nrc_reg_et_title);
        et_address = (EditText) findViewById(R.id.nrc_reg_et_add);
        et_city = (EditText) findViewById(R.id.nrc_reg_et_city);
        sp_state = (Spinner) findViewById(R.id.nrc_reg_et_state);
        et_pincode = (EditText) findViewById(R.id.nrc_reg_et_pincode);
        et_phone = (EditText) findViewById(R.id.nrc_reg_et_phone);
        et_reg_num = (EditText) findViewById(R.id.nrc_reg_et_reg_num);
        btn_browse = (Button) findViewById(R.id.nrc_reg_doc);
        tv_reg_doc_name = (TextView) findViewById(R.id.nrc_reg_doc_name);

        db = FirebaseFirestore.getInstance();
        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browsefile();
            }
        });

        db.collection("States")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.v("FIRESTOREEE", document.getId() + " => " + document.get("state"));


//                                Button btn = (Button) findViewById(R.id.btn);

                                // Initializing a String Array
//                                plants = new String[]{
//                                        (String) document.get("state")
//                                };
                                states.add((String) document.get("state"));


                                // This is what i tried. What should i write instead of these two lines bellow
                                // I can get the list of years with document.get("UP.Biologia") but i can't seem to find a way to populate the spinner with this...

                                // ArrayAdapter adapterYears = ArrayAdapter.createFromResource(ExamesActivity.this, document.get("UEM.Biologia"), android.R.layout.simple_spinner_dropdown_item);
                                // spinner.setAdapter(adapterYears);


                            }
                            final List<String> stateList = new ArrayList<>(states);

                            // Initializing an ArrayAdapter
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(NRCRegisterActivity.this, R.layout.spinner_item, stateList);

                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                            sp_state.setAdapter(spinnerArrayAdapter);
                        } else {
                            Log.v("FIRESTOREEE WARNING", "Error getting documents.", task.getException());
                        }
                    }
                });


//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,States);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
//        sp_state.setAdapter(arrayAdapter);
//        getStates();
//        DocumentSnapshot document = task.getResult();
//        List<String> group = (List<String>) document.get("States");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadfile();
            }
        });
    }

    private void getStates() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference statecollectionref = db.collection("States");
        Query statequery = statecollectionref.orderBy("state", Query.Direction.ASCENDING);
        Log.v("lll", "data aa gya" + statequery);
    }

    public void browsefile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a PDF"), PICK_PDF_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            filepath = data.getData();

            int cut = filepath.toString().lastIndexOf('/');
            if (cut != -1) {
                tv_reg_doc_name.setText(filepath.toString().substring(cut + 1));
            }
        }
    }

    private void uploadfile() {
        if (filepath != null) {

            final ProgressDialog progressdialog = new ProgressDialog(this);
            progressdialog.setTitle("Uploading....");
            progressdialog.show();
            final StorageReference regsRef = mStorageRef.child("nrcregpdf/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

            regsRef.putFile(filepath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return regsRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        IMainActivity iMainActivity = new IMainActivity();
                        iMainActivity.createNewNRC(getBaseContext(), userId, Integer.valueOf(et_bed_count.getText().toString()),
                                Integer.valueOf(et_bed_vacant.getText().toString()), et_title.getText().toString(),
                                downloadUri.toString(), et_reg_num.getText().toString(), et_address.getText().toString(), sp_state.getSelectedItem().toString(), et_city.getText().toString(),
                                Integer.valueOf(et_pincode.getText().toString()), et_phone.getText().toString(),
                                userEmail, false);
                    } else {
                        Toast.makeText(getApplicationContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
