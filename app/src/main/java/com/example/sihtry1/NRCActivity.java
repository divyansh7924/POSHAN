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

    private Button nrc_current_referrals;
    private Button nrc_admitted_children;
    private Button nrc_followups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrc);
        nrc_current_referrals = (Button)findViewById(R.id.nrc_current_ref);
        nrc_admitted_children = (Button) findViewById(R.id.nrc_admitted);
        nrc_followups = (Button)findViewById(R.id.nrc_followups);
        nrc_current_referrals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentref();
            }
        });
        nrc_admitted_children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admittedprofiles();
            }
        });
        nrc_followups.setOnClickListener(new View.OnClickListener() {
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
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
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
    public void currentref(){
        Intent intent = new Intent(this, CurrentReferralsActivity.class);
        startActivity(intent);
    }
    public void admittedprofiles(){
        Intent intent = new Intent(this, AdmittedChildrenActivity.class);
        startActivity(intent);
    }
    public void show_followups(){
        Intent intent = new Intent(this, FollowupsListActivity.class);
        startActivity(intent);
    }
}
