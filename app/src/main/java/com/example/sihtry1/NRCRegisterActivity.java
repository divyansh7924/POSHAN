package com.example.sihtry1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import java.net.MalformedURLException;
import java.net.URL;

public class NRCRegisterActivity extends AppCompatActivity {

    private Button submit;
    private EditText et_bed_count, et_bed_vacant, et_title, et_address, et_city, et_state, et_pincode, et_phone, et_reg_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("hello", "hello");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrc_register);

        submit = (Button) findViewById(R.id.nrc_reg_submit);
        et_bed_count = (EditText) findViewById(R.id.nrc_reg_et_bed_count);
        et_bed_vacant = (EditText) findViewById(R.id.nrc_reg_et_bed_vacant);
        et_title = (EditText) findViewById(R.id.nrc_reg_et_title);
        et_address = (EditText) findViewById(R.id.nrc_reg_et_add);
        et_city = (EditText) findViewById(R.id.nrc_reg_et_city);
        et_state = (EditText) findViewById(R.id.nrc_reg_et_state);
        et_pincode = (EditText) findViewById(R.id.nrc_reg_et_pincode);
        et_phone = (EditText) findViewById(R.id.nrc_reg_et_phone);
        et_reg_num = (EditText) findViewById(R.id.nrc_reg_et_reg_num);

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
                        url.toString(), et_reg_num.getText().toString(), et_address.getText().toString(), et_state.getText().toString(), et_city.getText().toString(),
                        Integer.valueOf(et_pincode.getText().toString()), et_phone.getText().toString(),
                        userEmail, false);
            }
        });
    }

}
