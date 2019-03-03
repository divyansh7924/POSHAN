package com.example.sihtry1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private Button register, login;
    private EditText register_et_email, register_et_password;
    private RadioButton nrc, rcr;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        login = (Button) findViewById(R.id.register_btn_login);
        register = (Button) findViewById(R.id.register_btn_register);
        register_et_email = (EditText) findViewById(R.id.register_et_email);
        register_et_password = (EditText) findViewById(R.id.register_et_password);
        nrc = (RadioButton) findViewById(R.id.register_radio_nrc);
        rcr = (RadioButton) findViewById(R.id.register_radio_rcr);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register_nrc() {
        Intent intent = new Intent(this, NRCRegisterActivity.class);
        startActivity(intent);
    }

    private void register() {
        String email, password;
        email = register_et_email.getText().toString().trim();
        password = register_et_password.getText().toString();

        if (email.isEmpty()) {
            register_et_email.setError("Cannot be empty");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            register_et_email.setError("Not Valid Email");
            return;
        }
        if (password.isEmpty() || password.length() < 6) {
            register_et_password.setError("Must be 6 or more character long");
            return;
        }
        if (!(nrc.isChecked() || rcr.isChecked())) {
            Toast.makeText(getApplicationContext(), "Select NRC or Anganwadi", Toast.LENGTH_SHORT).show();
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (nrc.isChecked()) {
                                register_nrc();
                            } else {
                                register_rcr();
                            }

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "Email already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void register_rcr() {
        Intent intent = new Intent(this, RCRRegisterActivity.class);
        startActivity(intent);
    }

    private void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
//
//    private void loginUpdateUI(FirebaseUser user) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("userobject", user);
//        startActivity(intent);
//        finish();
//    }
}
