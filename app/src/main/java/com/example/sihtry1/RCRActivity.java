package com.example.sihtry1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class RCRActivity extends AppCompatActivity {

    private Button btn_create_new_referral, btn_updateref, btn_ListofNRC, btn_z_zcore, btn_settings, btn_records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcr);

        btn_create_new_referral = (Button) findViewById(R.id.rcr_btn_create_new_ref);
        btn_ListofNRC = (Button) findViewById(R.id.rcr_btn_nrc_list);
        btn_updateref = (Button) findViewById(R.id.rcr_btn_update_profile);
        btn_z_zcore = (Button) findViewById(R.id.rcr_btn_z_score);
        btn_settings = findViewById(R.id.rcr_settings);
        btn_records = findViewById(R.id.rcr_past_records);

        btn_ListofNRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listofnrc();
            }
        });
        btn_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pastrecords();
            }
        });
        btn_create_new_referral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerChild();
            }
        });
        btn_updateref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agnupdateref();
            }
        });
        btn_z_zcore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zscoreActivity();
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsActivity();
            }
        });
    }

    private void zscoreActivity() {
        Intent intent = new Intent(this, ZScoreActivity.class);
        startActivity(intent);
    }

    private void registerChild() {
        Intent intent = new Intent(this, CreateReferralActivity.class);
        startActivity(intent);
    }

    private void listofnrc() {
        Intent intent = new Intent(this, StatesActivity.class);
        startActivity(intent);
    }

    private void agnupdateref() {
        Intent intent = new Intent(this, AgnUpdateReferralActivity.class);
        startActivity(intent);
    }

    private void settingsActivity() {
        Intent intent = new Intent(this, RCRUpdateActivity.class);
        startActivity(intent);
    }

    private void pastrecords() {
        Intent intent = new Intent(this, RcrPastRecordsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.main_menu_signout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
