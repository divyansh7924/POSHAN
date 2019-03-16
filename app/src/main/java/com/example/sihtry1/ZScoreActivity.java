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
    private double[] table_boy;
    private double[] table_girl;
    private int hight;
    private double weight;

    public ZScoreActivity(){
    	table_boy= new double[]{1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.6, 2.7, 2.9, 3.1, 3.3, 3.6, 3.8, 4.0, 4.3, 4.5, 4.7, 4.9, 5.1, 5.3, 5.5, 5.7, 5.9, 6.1, 6.3, 6.5, 6.6, 6.8, 7.0, 7.2, 7.3, 7.5, 7.6, 7.8, 7.9, 8.1, 8.2, 8.4, 8.6, 8.7, 8.9, 9.1, 9.3, 9.6, 9.8, 10.0, 10.2, 10.4, 10.6, 10.8, 11.0, 11.1, 11.3, 11.5, 11.7, 11.9, 12.1, 12.3, 12.5, 12.7, 13.0, 13.2, 13.4, 13.7, 13.9, 14.1, 14.4, 14.6, 14.9, 15.2, 15.4, 15.7, 16.0, 16.2, 16.5, 16.8, 17.1};
    	table_girl= new double[]{1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.6, 2.8, 2.9, 3.1, 3.3, 3.5, 3.7, 3.9, 4.1, 4.3, 4.5, 4.7, 4.9, 5.1, 5.3, 5.5, 5.6, 5.8, 6.0, 6.1, 6.3, 6.5, 6.6, 6.8, 6.9, 7.1, 7.2, 7.4, 7.5, 7.7, 7.8, 8.0, 8.1, 8.3, 8.5, 8.7, 8.9, 9.2, 9.4, 9.6, 9.8, 10.0, 10.2, 10.4, 10.6, 10.8, 10.9, 11.1, 11.3, 11.5, 11.7, 12.0, 12.2, 12.4, 12.6, 12.9, 13.1, 13.4, 13.7, 13.9, 14.2, 14.5, 14.8, 14.9, 15.1, 15.4, 15.7, 16.0, 16.3, 16.6, 16.9, 17.3};
    	hight= round_off(Double.parseDouble(et_height.getText().toString()));
        weight= Double.parseDouble(et_weight.getText().toString());
	    }

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
        else if(zscore(gender_set())==true){
        	setSAM();
        }
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

	private int round_off(double hight_temp) {
    	if ((hight_temp - (int) hight_temp) < 0.5) {
            hight_temp = (int) hight_temp;
        } else {
            hight_temp = (int) hight_temp + 1;
        }
        return (int) hight_temp;
    }

    private boolean zscore(char gender)
    {
    	int temp = 0;
    	boolean decider = false;
        temp = (int) (hight - 45);
        if (gender == 'm')
        {
            if (table_boy[temp] >=weight)
            {
                decider=true;
            }
            else if (gender == 'f')
            {
                if (table_girl[temp] >=weight)
                {
                    decider=true;
                }
            }
            else
                {
               decider= false;
                }
        }
        return decider;

    }

    private char gender_set()
    {
    	char gender;
		if (rb_child_male.isChecked())
		 {
		 	gender = 'm';
            }
        else if (rb_child_female.isChecked())
         {
         gender = 'f';
         }
          else
           {
          gender = 'o';
          }
    return gender;
    }
}
