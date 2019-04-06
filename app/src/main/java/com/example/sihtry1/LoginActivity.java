package com.example.sihtry1;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Build;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sihtry1.models.NRC;
import com.example.sihtry1.models.RCR;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private Button login_btn_login;
    private Button login_btn_register;
    private EditText login_et_email;
    private EditText login_et_password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        login_btn_login = (Button) findViewById(R.id.login_btn_login);
        login_btn_register = (Button) findViewById(R.id.login_btn_register);
        login_et_email = (EditText) findViewById(R.id.login_et_email);
        login_et_password = (EditText) findViewById(R.id.login_et_password);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        login_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        login_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loginUpdateUI(currentUser);
        }
    }

    private void login() {
        String email, password;
        email = login_et_email.getText().toString().trim();
        password = login_et_password.getText().toString();

        if (email.isEmpty()) {
            login_et_email.setError("Cannot be empty");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login_et_email.setError("Not Valid Email");
            return;
        }
        if (password.isEmpty() || password.length() < 6) {
            login_et_password.setError("Must be 6 or more character long");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginActivity.java", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginUpdateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LoginActivity.java", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loginUpdateUI(FirebaseUser user) {
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference nrcCollecRef = db.collection("nrc");
        Query nrcQuery = nrcCollecRef.whereEqualTo("user_id", userId);

        final ArrayList<NRC> nrcList = new ArrayList<>();
        nrcQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        nrcList.add(documentSnapshot.toObject(NRC.class));
                    }

                    try {
                        if (nrcList.get(0).isVerified()) {
                            Log.v("IMainActivity", "hello1");
                            nrcVerified();
                        } else {
                            verificationDue();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.v("IMainAcitivity", "Task Not Completed");
                }
            }
        });

        CollectionReference rcrCollecRef = db.collection("rcr");
        Query rcrQuery = rcrCollecRef.whereEqualTo("user_id", userId);

        final ArrayList<RCR> rcrList = new ArrayList<>();
        rcrQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        rcrList.add(documentSnapshot.toObject(RCR.class));
                    }

                    try {
                        if (rcrList.get(0).isVerified()) {
                            Log.v("IMainActivity", "hello2");
                            rcrVerified();
                        } else {
                            verificationDue();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.v("IMainAcitivity", "Task Not Completed");
                }
            }
        });

    }

    private void rcrVerified() {
        Intent intent = new Intent(getApplicationContext(), RCRActivity.class);
        startActivity(intent);
        finish();
    }

    private void nrcVerified() {
        Intent intent = new Intent(getApplicationContext(), NRCActivity.class);
        startActivity(intent);
        finish();
    }

    private void verificationDue() {
        Intent intent = new Intent(getApplicationContext(), VerificationDueActivity.class);
        startActivity(intent);
        finish();
    }


    private void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


}