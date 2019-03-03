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

    private Button btn_create_new_referral;
    private Button btn_ListofNRC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcr);

        btn_create_new_referral = (Button) findViewById(R.id.rcr_btn_create_new_ref);
        btn_ListofNRC = (Button)findViewById(R.id.rcr_btn_nrc_list);


        btn_ListofNRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listofnrc();
            }
        });
        btn_create_new_referral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerChild();
            }
        });
    }

    private void registerChild() {
        Intent intent = new Intent(this, CreateReferralActivity.class);
        startActivity(intent);
    }

    private void listofnrc() {
        Intent intent = new Intent(this, StatesActivity.class);
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
