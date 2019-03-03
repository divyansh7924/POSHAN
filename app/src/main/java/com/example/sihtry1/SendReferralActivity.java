package com.example.sihtry1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SendReferralActivity extends AppCompatActivity {

    String selectednrc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_referral);
        Intent intent = getIntent();
        selectednrc = intent.getStringExtra("message");
        Toast.makeText(SendReferralActivity.this, "str" + selectednrc, Toast.LENGTH_LONG).show();
    }
}
