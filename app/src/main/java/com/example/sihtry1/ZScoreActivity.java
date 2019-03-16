package com.example.sihtry1;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class ZScoreActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner oedema_spinner;
    private int oedema_stage = 0;
    private Button btn_check_status, btn_create_referral;
    private TextView tv_zscore, tv_status;
    private EditText et_age, et_height, et_weight, et_muac;
    private RadioButton rb_child_male, rb_child_female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zscore);

        btn_create_referral = findViewById(R.id.zscore_btn_create_referral);
        btn_check_status = findViewById(R.id.zscore_btn_check_status);
        tv_zscore = findViewById(R.id.zscore_tv_zscore);
        et_age = findViewById(R.id.zscore_et_age);
        tv_status = findViewById(R.id.zscore_tv_status);
        et_weight = findViewById(R.id.zscore_et_weight);
        et_height = findViewById(R.id.zscore_et_height);
        et_muac = findViewById(R.id.zscore_et_muac);
        rb_child_female = findViewById(R.id.zscore_rb_female);
        rb_child_male = findViewById(R.id.zscore_rb_male);

        btn_check_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus();
            }
        });

        btn_create_referral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerChild();
            }
        });

        addListenerOnSpinner();
    }

    private void checkStatus() {
        int age;
        float weight, height, muac;

        age = Integer.parseInt(et_age.getText().toString());
        weight = Float.parseFloat(et_weight.getText().toString());
        height = Float.parseFloat(et_height.getText().toString());
        muac = Float.parseFloat(et_muac.getText().toString());

        if (oedema_stage > 0) {
            setSAM();
        } else if (age > 6 && muac < 115) {
            setSAM();
        }
        // TODO(1): add another condition to check for SAM based on WH Z Score
        else {
            setNotSAM();
        }
    }


    private void setSAM() {
        tv_status.setVisibility(View.VISIBLE);
        tv_status.setText("Child is malnourished");
        tv_status.setTextColor(Color.RED);
        btn_create_referral.setVisibility(View.VISIBLE);
    }

    private void setNotSAM() {
        tv_status.setVisibility(View.VISIBLE);
        tv_status.setText("Child is not malnourished");
        tv_status.setTextColor(Color.GREEN);
        btn_create_referral.setVisibility(View.INVISIBLE);
    }

    private void registerChild() {
        Intent intent = new Intent(this, CreateReferralActivity.class);
        startActivity(intent);
    }

    private void addListenerOnSpinner() {
        oedema_spinner = findViewById(R.id.zscore_spinner_oedema);
        oedema_spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        oedema_stage = position - 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
