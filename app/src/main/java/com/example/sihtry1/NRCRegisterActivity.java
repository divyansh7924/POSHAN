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
import android.widget.Toast;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NRCRegisterActivity extends AppCompatActivity {

    private Button submit;
    public FirebaseFirestore db;
    private EditText et_bed_count, et_bed_vacant, et_title, et_address, et_city, et_pincode, et_phone, et_reg_num;
    private Spinner sp_state;
    private ArrayList<String> States = new ArrayList<>();
    private Button browse;
    private StorageReference mStorageRef;
    private static final int PICK_PDF_REQUEST = 234;
    private Uri filepath;
//    public String[] plants[];
    ArrayList<String> plants=new ArrayList<String>(25);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("hello", "hello");
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

        browse = (Button)findViewById(R.id.nrc_reg_doc);

        db = FirebaseFirestore.getInstance();
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browsefile();
            }
        });

        db.collection("States")
                .get()
                .addOnCompleteListener(new OnCompleteListener< QuerySnapshot >() {
                    @Override
                    public void onComplete(@NonNull Task< QuerySnapshot > task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document: task.getResult()) {
                                Log.v("FIRESTOREEE", document.getId() + " => " + document.get("state"));


//                                Button btn = (Button) findViewById(R.id.btn);

                                // Initializing a String Array
//                                plants = new String[]{
//                                        (String) document.get("state")
//                                };
                                plants.add((String) document.get("state"));



                                // This is what i tried. What should i write instead of these two lines bellow
                                // I can get the list of years with document.get("UP.Biologia") but i can't seem to find a way to populate the spinner with this...

                                // ArrayAdapter adapterYears = ArrayAdapter.createFromResource(ExamesActivity.this, document.get("UEM.Biologia"), android.R.layout.simple_spinner_dropdown_item);
                                // spinner.setAdapter(adapterYears);


                            }
                            final List<String> plantsList = new ArrayList<>(plants);

                            // Initializing an ArrayAdapter
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(NRCRegisterActivity.this,R.layout.spinner_item,plantsList);

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
                Log.v("NRCREGACT", "aa rha hai");
                IMainActivity iMainActivity = new IMainActivity();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String url = "https://stackoverflow.com/questions/6367509/how-to-use-intent-in-non-activity-class";
                iMainActivity.createNewNRC(getBaseContext(), userId, Integer.valueOf(et_bed_count.getText().toString()),
                        Integer.valueOf(et_bed_vacant.getText().toString()), et_title.getText().toString(),
                        url.toString(), et_reg_num.getText().toString(), et_address.getText().toString(), sp_state.getSelectedItem().toString(), et_city.getText().toString(),
                        Integer.valueOf(et_pincode.getText().toString()), et_phone.getText().toString(),
                        userEmail, false);

                uploadfile();
            }
        });
    }
    private void getStates(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference statecollectionref = db.collection("States");

        Query statequery = statecollectionref.orderBy("state", Query.Direction.ASCENDING);
        Log.v("lll","data aa gya"+statequery);



    }

    public void browsefile(){
        Intent intent= new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select a PDF"), PICK_PDF_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_PDF_REQUEST && resultCode == RESULT_OK && data.getData() != null){
            filepath = data.getData();
        }
    }

    private void uploadfile() {
        if (filepath != null) {

            final ProgressDialog progressdialog = new ProgressDialog(this);
            progressdialog.setTitle("Uploading....");
            progressdialog.show();
            StorageReference regsRef = mStorageRef.child("nrcregpdf/" + et_title.getText());

            regsRef.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressdialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Application uploaded for review", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressdialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressdialog.setMessage(((int)progress) + " % uploaded...");
                        }
                    });
        }
    }

}
