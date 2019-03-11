package com.example.sihtry1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;

public class RCRRegisterActivity extends AppCompatActivity {

    private Button submit;
    private Button browse;
    public FirebaseFirestore db;
    private Spinner sp_state;
    private StorageReference mStorageRef;
    private static final int PICK_PDF_REQUEST = 234;
    private Uri filepath;
    private EditText et_bed_count, et_bed_vacant, et_title, et_address, et_city, et_pincode, et_phone, et_reg_num;
    private Uri downloadUri;
    ArrayList<String> states = new ArrayList<String>(25);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcr_register);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        submit = (Button) findViewById(R.id.rcr_reg_submit);
        et_title = (EditText) findViewById(R.id.rcr_reg_et_title);
        et_address = (EditText) findViewById(R.id.rcr_reg_et_add);
        et_city = (EditText) findViewById(R.id.rcr_reg_et_city);
        sp_state = (Spinner) findViewById(R.id.rcr_reg_et_state);
        et_pincode = (EditText) findViewById(R.id.rcr_reg_et_pincode);
        et_phone = (EditText) findViewById(R.id.rcr_reg_et_phone);
        et_reg_num = (EditText) findViewById(R.id.rcr_reg_et_reg_num);

        browse = (Button) findViewById(R.id.rcr_reg_doc);
        db = FirebaseFirestore.getInstance();
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browsefile();
            }
        });

        db.collection("States")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task< QuerySnapshot > task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document: task.getResult()) {
                                Log.v("FIRESTOREEE", document.getId() + " => " + document.get("state"));


                                states.add((String) document.get("state"));





                            }
                            final List<String> statesList = new ArrayList<>(states);

                            // Initializing an ArrayAdapter
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(RCRRegisterActivity.this,R.layout.spinner_item,statesList);

                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                            sp_state.setAdapter(spinnerArrayAdapter);
                        } else {
                            Log.v("FIRESTOREEE WARNING", "Error getting documents.", task.getException());
                        }
                    }
                });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadfile();
            }
        });
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
        }
    }

    private void uploadfile() {
        if (filepath != null) {

            final ProgressDialog progressdialog = new ProgressDialog(this);
            progressdialog.setTitle("Uploading....");
            progressdialog.show();
            final StorageReference regsRef = mStorageRef.child("rcrregpdf/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

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
                        iMainActivity.createNewRCR(getApplicationContext(), userId, et_title.getText().toString(),
                                downloadUri.toString(), et_reg_num.getText().toString(), et_address.getText().toString(), sp_state.getSelectedItem().toString(),
                                et_city.getText().toString(), Integer.parseInt(et_pincode.getText().toString()), et_phone.getText().toString(), userEmail, false);
                    } else {
                        Toast.makeText(getApplicationContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
