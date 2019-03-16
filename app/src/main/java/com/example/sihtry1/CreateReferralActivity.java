package com.example.sihtry1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sihtry1.models.Referral;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateReferralActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int mYear, mMonth, mDay;
    private Button create_referral_btn_submit;
    private EditText create_referral_et_parent_name, create_referral_et_child_f_name, create_referral_et_child_l_name, create_referral_et_bloodgp,
            create_referral_et_city, create_referral_et_ashamsmt, create_referral_et_pincode, create_referral_et_height, create_referral_et_symptoms,
            create_referral_et_weight, create_referral_et_aadhaar_parent, create_referral_et_aadhaar_child, create_referral_et_add, create_referral_et_phone;
    private RadioButton create_referral_rb_child_male, create_referral_rb_child_female;
    private int day_of_birth, month_of_birth, year_of_birth;
    private Spinner spinner_oedema;
    private Spinner sp_state;
    public FirebaseFirestore db;
    ArrayList<String> states = new ArrayList<String>(25);
    private int oedema_stage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_referral);

        create_referral_btn_submit = (Button) findViewById(R.id.create_referral_btn_submit);
        create_referral_et_parent_name = (EditText) findViewById(R.id.create_referral_et_parent_name);
        create_referral_et_child_f_name = (EditText) findViewById(R.id.create_referral_et_child_f_name);
        create_referral_et_child_l_name = (EditText) findViewById(R.id.create_referral_et_child_l_name);
        create_referral_et_bloodgp = (EditText) findViewById(R.id.create_referral_et_bloodgp);
        create_referral_et_city = (EditText) findViewById(R.id.create_referral_et_city);
        sp_state = (Spinner) findViewById(R.id.create_referral_et_state);
        create_referral_et_ashamsmt = (EditText) findViewById(R.id.create_referral_et_ashamsmt);
        create_referral_et_pincode = (EditText) findViewById(R.id.create_referral_et_pin);
        create_referral_et_symptoms = (EditText) findViewById(R.id.create_referral_et_symptoms);
        create_referral_et_height = (EditText) findViewById(R.id.create_referral_et_height);
        create_referral_et_weight = (EditText) findViewById(R.id.create_referral_et_weight);
        create_referral_et_aadhaar_parent = (EditText) findViewById(R.id.create_referral_et_aadhaar_parent);
        create_referral_et_aadhaar_child = (EditText) findViewById(R.id.create_referral_et_aadhaar_child);
        create_referral_et_add = (EditText) findViewById(R.id.create_referral_et_add);
        create_referral_et_phone = (EditText) findViewById(R.id.create_referral_et_phone);
        create_referral_rb_child_male = (RadioButton) findViewById(R.id.create_referral_rb_child_male);
        create_referral_rb_child_female = (RadioButton) findViewById(R.id.create_referral_rb_child_female);
        spinner_oedema = (Spinner) findViewById(R.id.create_referral_spinner_oedema);
        spinner_oedema.setOnItemSelectedListener(this);
        db = FirebaseFirestore.getInstance();


        create_referral_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = null;
                if (create_referral_rb_child_male.isChecked()) {
                    gender = "m";
                } else if (create_referral_rb_child_female.isChecked()) {
                    gender = "f";
                } else {
                    Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                    return;
                }


                createNewReferral();
            }
        });

        db.collection("States")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.v("FIRESTOREEE", document.getId() + " => " + document.get("state"));


                                states.add((String) document.get("state"));


                            }
                            final List<String> statesList = new ArrayList<>(states);

                            // Initializing an ArrayAdapter
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CreateReferralActivity.this, R.layout.spinner_item, statesList);

                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                            sp_state.setAdapter(spinnerArrayAdapter);
                        } else {
                            Log.v("FIRESTOREEE WARNING", "Error getting documents.", task.getException());
                        }
                    }
                });
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadfile();
//            }
//        });

        final Button pickDate = (Button) findViewById(R.id.pick_date);
        final TextView textView = (TextView) findViewById(R.id.date);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // myCalendar.add(Calendar.DATE, 0);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                textView.setText(sdf.format(myCalendar.getTime()));
            }
        };

        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(CreateReferralActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                if (year < mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear, mMonth, mDay);

                                textView.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                day_of_birth = dayOfMonth;
                                month_of_birth = monthOfYear;
                                year_of_birth = year;
                                Log.v("CreateReferralActivity", String.valueOf(day_of_birth));
                                Log.v("CreateReferralActivity", String.valueOf(month_of_birth));
                                Log.v("CreateReferralActivity", String.valueOf(year_of_birth));

                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(1990);
                dpd.show();

            }
        });
    }

    private void createNewReferral() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newReferralRef = db.collection("referral").document();
        Referral referral = new Referral();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String referralid = dateFormat.format(date);
        referral.setReferral_id(referralid);

        referral.setChild_first_name(create_referral_et_child_f_name.getText().toString());

        referral.setChild_last_name(create_referral_et_child_l_name.getText().toString());

        if (create_referral_rb_child_male.isChecked()) {
            referral.setChild_gender("m");
        } else if (create_referral_rb_child_female.isChecked()) {
            referral.setChild_gender("f");
        } else {
            referral.setChild_gender("o");
        }

        referral.setDay_of_birth(day_of_birth);

        referral.setMonth_of_birth(month_of_birth);

        referral.setYear_of_birth(year_of_birth);

        referral.setBlood_group(create_referral_et_bloodgp.getText().toString());

        referral.setAsha_measure(Float.parseFloat(create_referral_et_ashamsmt.getText().toString()));

        referral.setHeight(Float.parseFloat(create_referral_et_height.getText().toString()));

        referral.setWeight(Float.parseFloat(create_referral_et_weight.getText().toString()));

        referral.setOedema(oedema_stage);

        referral.setGuadian_name(create_referral_et_parent_name.getText().toString());

        referral.setGuardian_aadhaar_num(create_referral_et_aadhaar_parent.getText().toString());

        referral.setNrc_id(null);

        referral.setRcr_id(userid);

        referral.setOther_symptoms(create_referral_et_symptoms.getText().toString());

        referral.setPhone(create_referral_et_phone.getText().toString());

        referral.setState(sp_state.getSelectedItem().toString());

        referral.setCity(create_referral_et_city.getText().toString());

        referral.setPincode(Integer.parseInt(create_referral_et_pincode.getText().toString()));

        referral.setAddress(create_referral_et_add.getText().toString());

        referral.setChild_aadhaar_num(create_referral_et_aadhaar_child.getText().toString());

        referral.setStatus("Created");


        newReferralRef.set(referral).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), RCRActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Registeration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        oedema_stage = position - 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}