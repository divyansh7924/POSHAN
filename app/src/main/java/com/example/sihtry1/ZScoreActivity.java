package com.example.sihtry1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ZScoreActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner oedema_spinner;
    private int oedema_stage = -1, cyear, cmonth, cday, age = 0, height, muac;
    private Button btn_check_status, btn_create_referral;
    private TextView tv_status, tv_date;
    private EditText et_height, et_weight, et_muac;
    private RadioButton rb_child_male, rb_child_female;
    private double[] table_boy;
    private double[] table_girl;
    char gender;
    int child_age = 0;
    int current_age = 0;
    float weight;

    private static final String TAG = "ZScoreActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zscore);

        btn_create_referral = findViewById(R.id.zscore_btn_create_referral);
        btn_check_status = findViewById(R.id.zscore_btn_check_status);
        tv_status = findViewById(R.id.zscore_tv_status);
        et_weight = findViewById(R.id.zscore_et_weight);
        et_height = findViewById(R.id.zscore_et_height);
        tv_date = findViewById(R.id.zscore_tv_date);
        et_muac = findViewById(R.id.zscore_et_muac);
        rb_child_female = findViewById(R.id.zscore_rb_female);
        rb_child_male = findViewById(R.id.zscore_rb_male);

        table_boy = new double[]{1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.6, 2.7, 2.9, 3.1, 3.3, 3.6, 3.8,
                4.0, 4.3, 4.5, 4.7, 4.9, 5.1, 5.3, 5.5, 5.7, 5.9, 6.1, 6.3, 6.5, 6.6, 6.8, 7.0,
                7.2, 7.3, 7.5, 7.6, 7.8, 7.9, 8.1, 8.2, 8.4, 8.6, 8.7, 8.9, 9.1, 9.3, 9.6, 9.8,
                10.0, 10.2, 10.4, 10.6, 10.8, 11.0, 11.1, 11.3, 11.5, 11.7, 11.9, 12.1, 12.3, 12.5,
                12.7, 13.0, 13.2, 13.4, 13.7, 13.9, 14.1, 14.4, 14.6, 14.9, 15.2, 15.4, 15.7, 16.0,
                16.2, 16.5, 16.8, 17.1};
        table_girl = new double[]{1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.6, 2.8, 2.9, 3.1, 3.3, 3.5, 3.7,
                3.9, 4.1, 4.3, 4.5, 4.7, 4.9, 5.1, 5.3, 5.5, 5.6, 5.8, 6.0, 6.1, 6.3, 6.5, 6.6, 6.8,
                6.9, 7.1, 7.2, 7.4, 7.5, 7.7, 7.8, 8.0, 8.1, 8.3, 8.5, 8.7, 8.9, 9.2, 9.4, 9.6, 9.8,
                10.0, 10.2, 10.4, 10.6, 10.8, 10.9, 11.1, 11.3, 11.5, 11.7, 12.0, 12.2, 12.4, 12.6,
                12.9, 13.1, 13.4, 13.7, 13.9, 14.2, 14.5, 14.8, 14.9, 15.1, 15.4, 15.7, 16.0, 16.3,
                16.6, 16.9, 17.3};

        btn_check_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!rb_child_female.isChecked() && !rb_child_male.isChecked()) {
                    Toast.makeText(ZScoreActivity.this, "Select Gender", Toast.LENGTH_SHORT).show();
                } else if ((current_age - child_age) < 1) {
                    Toast.makeText(ZScoreActivity.this, "Select Valid Age", Toast.LENGTH_SHORT).show();
                } else if (et_height.getText().toString().equals("")) {
                    Toast.makeText(ZScoreActivity.this, "Enter Height", Toast.LENGTH_SHORT).show();
                } else if (et_weight.getText().toString().equals("")) {
                    Toast.makeText(ZScoreActivity.this, "Enter Weight", Toast.LENGTH_SHORT).show();
                } else if (et_muac.getText().toString().equals("")) {
                    Toast.makeText(ZScoreActivity.this, "Enter MUAC", Toast.LENGTH_SHORT).show();
                } else if (oedema_stage < 0) {
                    Toast.makeText(ZScoreActivity.this, "Select Oedema Stage", Toast.LENGTH_SHORT).show();
                } else {
                    gender_set();
                    weight = Float.parseFloat(et_weight.getText().toString());
                    height = Integer.parseInt(et_height.getText().toString());
                    muac = Integer.parseInt(et_muac.getText().toString());

                    checkStatus();
                }
            }
        });


        //date picker Dialogue code starts
        final Button date = findViewById(R.id.btn_z_score);
        final Calendar z_score_calander = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener z_score_date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                z_score_calander.set(Calendar.YEAR, year);
                z_score_calander.set(Calendar.MONTH, month);
                z_score_calander.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String z_score_date = "yyyy-mm-dd";
                SimpleDateFormat z_date = new SimpleDateFormat(z_score_date, Locale.US);
                tv_date.setText(z_date.format(z_score_calander.getTime()));
            }
        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calc = Calendar.getInstance();
                cyear = calc.get(Calendar.YEAR);
                cmonth = calc.get(Calendar.MONTH);
                cday = calc.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datepicker = new DatePickerDialog(ZScoreActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int yr, int mth, int dom) {


                                if (yr < cyear)
                                    view.updateDate(cyear, cmonth, cday);

                                if (mth < cmonth && yr == cyear)
                                    view.updateDate(cyear, cmonth, cday);

                                if (dom < cday && yr == cyear && mth == cmonth)
                                    view.updateDate(cyear, cmonth, cday);

                                tv_date.setText(dom + "-" + (mth + 1) + "-" + yr);

                                child_age = ((365 * yr) + (yr / 4) - (yr / 100) + (yr / 400) + dom + (((153 * mth) + 8) / 5));
                                current_age = ((365 * cyear) + (cyear / 4) - (cyear / 100) + (cyear / 400) + cday + (((153 * cmonth) + 8) / 5));
                                age = (int) ((current_age - child_age) / 30.5);

                            }
                        }, cyear, cmonth, cday);
                datepicker.getDatePicker().setMinDate(1999);
                datepicker.show();
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
        if (oedema_stage > 0) {
            setSAM();
        } else if (age >= 6 && muac < 115) {
            setSAM();
        } else if (checkZScore()) {
            setSAM();
        } else {
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

    private boolean checkZScore() {
        boolean sam = false;
        int temp = (height - 45);
        if (gender == 'm' && table_boy[temp] >= weight) {
            sam = true;
        } else if (gender == 'f' && table_girl[temp] >= weight) {
            sam = true;
        }
        return sam;
    }

    private void gender_set() {
        gender = 'o';
        if (rb_child_male.isChecked()) {
            gender = 'm';
        } else if (rb_child_female.isChecked()) {
            gender = 'f';
        }
    }
}