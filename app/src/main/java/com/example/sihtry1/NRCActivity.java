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

public class NRCActivity extends AppCompatActivity {

    private Button btn_current_referrals, btn_admitted_children, btn_followups, btn_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrc);
        btn_current_referrals = (Button) findViewById(R.id.nrc_current_ref);
        btn_admitted_children = (Button) findViewById(R.id.nrc_admitted);
        btn_followups = (Button) findViewById(R.id.nrc_followups);
        btn_settings = findViewById(R.id.nrc_settings);

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateActivity();
            }
        });
        btn_current_referrals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentref();
            }
        });
        btn_admitted_children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admittedprofiles();
            }
        });
        btn_followups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_followups();
            }
        });

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

    public void currentref() {
        Intent intent = new Intent(this, CurrentReferralsActivity.class);
        startActivity(intent);
    }

    public void updateActivity() {
        Intent intent = new Intent(this, NRCUpdateActivity.class);
        startActivity(intent);
    }

    public void admittedprofiles() {
        Intent intent = new Intent(this, AdmittedChildrenActivity.class);
        startActivity(intent);
    }

    public void show_followups() {
        Intent intent = new Intent(this, FollowupsListActivity.class);
        startActivity(intent);
    }
}
