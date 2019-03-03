package com.example.sihtry1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import android.widget.Toast;
import java.io.File;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;

public class RCRRegisterActivity extends AppCompatActivity {

    private Button submit;
    private Button browse;
    private StorageReference mStorageRef;
    private static final int PICK_PDF_REQUEST = 234;
    private Uri filepath;
    private EditText et_bed_count, et_bed_vacant, et_title, et_address, et_city, et_state, et_pincode, et_phone, et_reg_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcr_register);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        submit = (Button) findViewById(R.id.rcr_reg_submit);
        et_title = (EditText) findViewById(R.id.rcr_reg_et_title);
        et_address = (EditText) findViewById(R.id.rcr_reg_et_add);
        et_city = (EditText) findViewById(R.id.rcr_reg_et_city);
        et_state = (EditText) findViewById(R.id.rcr_reg_et_state);
        et_pincode = (EditText) findViewById(R.id.rcr_reg_et_pincode);
        et_phone = (EditText) findViewById(R.id.rcr_reg_et_phone);
        et_reg_num = (EditText) findViewById(R.id.rcr_reg_et_reg_num);

        browse = (Button)findViewById(R.id.rcr_reg_doc);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browsefile();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("NRCREGACT", "aa rha hai");
                IMainActivity iMainActivity = new IMainActivity();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String url = " ";
                iMainActivity.createNewRCR(getApplicationContext(), userId, et_title.getText().toString(),
                        url, et_reg_num.getText().toString(), et_address.getText().toString(), et_state.getText().toString(), et_city.getText().toString(), Integer.parseInt(et_pincode.getText().toString()), et_phone.getText().toString(), userEmail, false);

                uploadfile();
            }
        });
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
