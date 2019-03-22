package com.example.sihtry1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
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
    private int oedema_stage = 0, cyear, cmonth, cday, hight_z, sema, et_age;
    private Button btn_check_status, btn_create_referral;
    private TextView tv_zscore, tv_status;
    private EditText et_height, et_weight, et_muac;
    private RadioButton rb_child_male, rb_child_female;
    private double[] table_boy;
    private double[] table_girl;
    private boolean zscore;
    private double weight_z;

    public ZScoreActivity() {
        table_boy = new double[]{1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.6, 2.7, 2.9, 3.1, 3.3, 3.6, 3.8, 4.0, 4.3, 4.5, 4.7, 4.9, 5.1, 5.3, 5.5, 5.7, 5.9, 6.1, 6.3, 6.5, 6.6, 6.8, 7.0, 7.2, 7.3, 7.5, 7.6, 7.8, 7.9, 8.1, 8.2, 8.4, 8.6, 8.7, 8.9, 9.1, 9.3, 9.6, 9.8, 10.0, 10.2, 10.4, 10.6, 10.8, 11.0, 11.1, 11.3, 11.5, 11.7, 11.9, 12.1, 12.3, 12.5, 12.7, 13.0, 13.2, 13.4, 13.7, 13.9, 14.1, 14.4, 14.6, 14.9, 15.2, 15.4, 15.7, 16.0, 16.2, 16.5, 16.8, 17.1};
        table_girl = new double[]{1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.6, 2.8, 2.9, 3.1, 3.3, 3.5, 3.7, 3.9, 4.1, 4.3, 4.5, 4.7, 4.9, 5.1, 5.3, 5.5, 5.6, 5.8, 6.0, 6.1, 6.3, 6.5, 6.6, 6.8, 6.9, 7.1, 7.2, 7.4, 7.5, 7.7, 7.8, 8.0, 8.1, 8.3, 8.5, 8.7, 8.9, 9.2, 9.4, 9.6, 9.8, 10.0, 10.2, 10.4, 10.6, 10.8, 10.9, 11.1, 11.3, 11.5, 11.7, 12.0, 12.2, 12.4, 12.6, 12.9, 13.1, 13.4, 13.7, 13.9, 14.2, 14.5, 14.8, 14.9, 15.1, 15.4, 15.7, 16.0, 16.3, 16.6, 16.9, 17.3};
        sema = 0;
        et_age=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zscore);

        btn_create_referral = findViewById(R.id.zscore_btn_create_referral);
        btn_check_status = findViewById(R.id.zscore_btn_check_status);
        tv_zscore = findViewById(R.id.zscore_tv_zscore);
        //et_age = findViewById(R.id.zscore_et_age);
        tv_status = findViewById(R.id.zscore_tv_status);
        et_weight = findViewById(R.id.zscore_et_weight);
        et_height = findViewById(R.id.zscore_et_height);
        et_muac = findViewById(R.id.zscore_et_muac);
        rb_child_female = findViewById(R.id.zscore_rb_female);
        rb_child_male = findViewById(R.id.zscore_rb_male);
        btn_check_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Error message display block
                /*try {
                    Double ag = Double.parseDouble(et_age.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(ZScoreActivity.this, "Please enter valid age ", Toast.LENGTH_SHORT).show();
                    recreate();
                    sema = 1;
                }*/
                try {
                    weight_z = Double.parseDouble(et_weight.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(ZScoreActivity.this, "Please enter valid weight ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }
                try {
                    hight_z = round_off(Double.parseDouble(et_height.getText().toString()));

                } catch (Exception e) {
                    Toast.makeText(ZScoreActivity.this, "Please enter valid hight ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }
                try {
                    Double mu = Double.parseDouble(et_muac.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(ZScoreActivity.this, "Please enter a valid Muac ", Toast.LENGTH_SHORT).show();
                    recreate();
                    sema = 1;
                }
                try {
                    zscore = zscore(gender_set());
                } catch (Exception e) {
                    Toast.makeText(ZScoreActivity.this, "Invalid hight or weight ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }
                try {
                    addListenerOnSpinner();
                } catch (Exception e) {
                    Toast.makeText(ZScoreActivity.this, "Invalid hight or weight ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }
                if (et_age<=0)
                {
                    Toast.makeText(ZScoreActivity.this, "Please enter valid age ", Toast.LENGTH_SHORT).show();
                    recreate();
                    sema = 1;
                }
                if (sema == 0) {
                    checkStatus();
                }
            }
        });


        //date picker Dialogue code starts
        final Button date = findViewById(R.id.btn_z_score);
        final TextView textView1 = findViewById(R.id.date_z_score);
        final Calendar z_score_calander = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener z_score_date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                z_score_calander.set(Calendar.YEAR, year);
                z_score_calander.set(Calendar.MONTH, month);
                z_score_calander.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String z_score_date = "yyyy-mm-dd";
                SimpleDateFormat z_date = new SimpleDateFormat(z_score_date, Locale.US);
                textView1.setText(z_date.format(z_score_calander.getTime()));
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
                                textView1.setText(dom + "-" + mth + "-" + yr);
                                et_age = (12 * (cyear - yr) + (cmonth - mth));
                                //textView1.setText(et_age);

                            }
                        },cyear,cmonth,cday);
                datepicker.getDatePicker().setMinDate(1999);
                datepicker.show();
            }
        });


        //DatePicker Dialogue Ends


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
        float muac;
        age = et_age;
        //age = Integer.parseInt(et_age.getText().toString());
        muac = Float.parseFloat(et_muac.getText().toString());

        if (oedema_stage > 0) {
            setSAM();
        } else if (oedema_stage < 0) {
            Toast.makeText(ZScoreActivity.this, "Please select oedema stage", Toast.LENGTH_SHORT).show();
        } else if (age >= 6 && muac < 115) {
            setSAM();
        } else if (zscore == true) {
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

    private void error() {
        tv_status.setVisibility(View.VISIBLE);
        tv_status.setText("Please enter vlaid details");
        tv_status.setTextColor(Color.RED);
        btn_create_referral.setVisibility(View.INVISIBLE);
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

    private int round_off(double hight_temp) {
        if ((hight_temp - (int) hight_temp) < 0.5) {
            hight_temp = (int) hight_temp;
        } else {
            hight_temp = (int) hight_temp + 1;
        }
        return (int) hight_temp;
    }

    private boolean zscore(char gender) {
        boolean decider = false;
        int temp = (hight_z - 45);
        if (gender == 'm') {
            if (table_boy[temp] >= weight_z) {
                decider = true;
            }
        } else if (gender == 'f') {
            if (table_girl[temp] >= weight_z) {
                decider = true;
            }
        } else {
            decider = false;
        }
        return decider;

    }

    private char gender_set() {
        char gender = 'o';
        if (rb_child_male.isChecked()) {
            gender = 'm';
        } else if (rb_child_female.isChecked()) {
            gender = 'f';
        } else {
            Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
        }
        return gender;
    }
}