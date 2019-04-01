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

    private int mYear, mMonth, mDay, age, oedema_stage = 0, day_of_birth, month_of_birth, year_of_birth,bloodgrp,controll=0,controll_1=0;
    private Button btn_submit;
    private EditText et_parent_name, et_child_f_name, et_child_l_name,
            et_ashamsmt, et_pincode, et_height, et_symptoms,
            et_weight, et_aadhaar_parent, et_aadhaar_child, et_phone, et_village, et_tehsil, et_district, et_treatedFor;
    private RadioButton rb_child_male, rb_child_female;
    private Spinner spinner_oedema, Blood_group, sp_state;
    public FirebaseFirestore db;
    ArrayList<String> states = new ArrayList<String>(25);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_referral);

        btn_submit = (Button) findViewById(R.id.create_referral_btn_submit);
        et_parent_name = (EditText) findViewById(R.id.create_referral_et_parent_name);
        et_child_f_name = (EditText) findViewById(R.id.create_referral_et_child_f_name);
        et_child_l_name = (EditText) findViewById(R.id.create_referral_et_child_l_name);
       // et_bloodgp = (EditText) findViewById(R.id.create_referral_et_bloodgp);
        sp_state = (Spinner) findViewById(R.id.create_referral_et_state);
        et_ashamsmt = (EditText) findViewById(R.id.create_referral_et_ashamsmt);
        et_pincode = (EditText) findViewById(R.id.create_referral_et_pin);
        et_symptoms = (EditText) findViewById(R.id.create_referral_et_symptoms);
        et_height = (EditText) findViewById(R.id.create_referral_et_height);
        et_weight = (EditText) findViewById(R.id.create_referral_et_weight);
        et_aadhaar_parent = (EditText) findViewById(R.id.create_referral_et_aadhaar_parent);
        et_aadhaar_child = (EditText) findViewById(R.id.create_referral_et_aadhaar_child);
        et_phone = (EditText) findViewById(R.id.create_referral_et_phone);
        rb_child_male = (RadioButton) findViewById(R.id.create_referral_rb_child_male);
        rb_child_female = (RadioButton) findViewById(R.id.create_referral_rb_child_female);
        spinner_oedema = (Spinner) findViewById(R.id.create_referral_spinner_oedema);
        et_tehsil = findViewById(R.id.create_referral_et_tehsil);
        et_village = findViewById(R.id.create_referral_et_village);
        et_district = findViewById(R.id.create_referral_et_district);
        et_treatedFor = findViewById(R.id.create_referral_et_treatedfor);

        spinner_oedema.setOnItemSelectedListener(this);
        db = FirebaseFirestore.getInstance();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sema = 0;
                try {
                    String name = et_parent_name.getText().toString();
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter valid name for guardian ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }
                try {
                    String name = et_child_f_name.getText().toString();
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid first name for child ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }
                try {
                    String name = et_child_l_name.getText().toString();
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid last name for child ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }
                /*try {
                    String name = et_child_l_name.getText().toString();
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid last name for child ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }*/

                try {
                    Float name = Float.parseFloat(et_ashamsmt.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid asha tape measurement for child ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }

                try {
                    Integer pin = Integer.parseInt(et_pincode.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid pincode", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }
                try {
                    String name = et_symptoms.getText().toString();
                } catch (Exception e) {
                    controll = 1;
                }
                try {
                    Float pin = Float.parseFloat(et_height.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid height ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }

                try {
                    Float pin = Float.parseFloat(et_weight.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid weight ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }

                try {
                    String name = et_aadhaar_parent.getText().toString();
                    if(name.length()<11)
                        throw new ArithmeticException();
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid aadhar number for parent ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }

                try {
                    String name = et_aadhaar_child.getText().toString();
                    if(name.length()<11)
                        throw new ArithmeticException();
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid aadhar number for child ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }

                try {
                    String name = et_tehsil.getText().toString();
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid tehsil ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }

                try {
                    String name = et_village.getText().toString();
                } catch (Exception e) {
                    controll_1 = 1;
                }

                try {
                    String name = et_district.getText().toString();
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid district ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }

                try {
                    String name = et_treatedFor.getText().toString();
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid treatment required ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }

                try {
                    String name = et_phone.getText().toString();
                    if(name.length()<9)
                        throw new ArithmeticException();
                } catch (Exception e) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid phone number ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }



                if(bloodgrp<0)
                {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a Blood Group ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }
                if(oedema_stage<0)
                {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a Blood Group ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }
                if (age <= 0) {
                    Toast.makeText(CreateReferralActivity.this, "Please enter a valid age for child ", Toast.LENGTH_SHORT).show();
                    sema = 1;
                }

                String gender = null;
                if (rb_child_male.isChecked()) {
                    gender = "m";
                } else if (rb_child_female.isChecked()) {
                    gender = "f";
                } else {
                    Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                    sema=1;
                    return;
                }


                if(sema==0) {
                    createNewReferral();
                }
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
                                age = (12 * (mYear - year) + (mMonth - monthOfYear));
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
        Listnerforbloodgroup();
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

        referral.setChild_first_name(et_child_f_name.getText().toString());

        referral.setChild_last_name(et_child_l_name.getText().toString());

        if (rb_child_male.isChecked()) {
            referral.setChild_gender("m");
        } else if (rb_child_female.isChecked()) {
            referral.setChild_gender("f");
        } else {
            referral.setChild_gender("o");
        }
        if(bloodgrp==0)
            referral.setBlood_group("A+");
        else if(bloodgrp==1)
            referral.setBlood_group("A-");
        else if(bloodgrp==2)
            referral.setBlood_group("B+");
        else if(bloodgrp==3)
            referral.setBlood_group("B-");
        else if(bloodgrp==4)
            referral.setBlood_group("AB+");
        else if(bloodgrp==5)
            referral.setBlood_group("AB-");
        else if(bloodgrp==6)
            referral.setBlood_group("O+");
        else if(bloodgrp==7)
            referral.setBlood_group("O-");

        referral.setDay_of_birth(day_of_birth);

        referral.setMonth_of_birth(month_of_birth);

        referral.setYear_of_birth(year_of_birth);

        //referral.setBlood_group(et_bloodgp.getText().toString());

        referral.setAsha_measure(Float.parseFloat(et_ashamsmt.getText().toString()));

        referral.setHeight(Float.parseFloat(et_height.getText().toString()));

        referral.setWeight(Float.parseFloat(et_weight.getText().toString()));

        referral.setOedema(oedema_stage);

        referral.setGuadian_name(et_parent_name.getText().toString());

        referral.setGuardian_aadhaar_num(et_aadhaar_parent.getText().toString());

        referral.setNrc_id(null);

        referral.setRcr_id(userid);

        if(controll==0)
        referral.setOther_symptoms(et_symptoms.getText().toString());
        else if(controll==1)
            referral.setOther_symptoms("Null");

        referral.setPhone(et_phone.getText().toString());

        referral.setState(sp_state.getSelectedItem().toString());

        referral.setDistrict(et_district.getText().toString());

        if(controll_1==0)
        referral.setVillage(et_village.getText().toString());
        else if(controll_1==1)
            referral.setVillage("Null");

        referral.setTehsil(et_tehsil.getText().toString());

        referral.setTreated_for(et_treatedFor.getText().toString());

        referral.setPincode(Integer.parseInt(et_pincode.getText().toString()));

        referral.setChild_aadhaar_num(et_aadhaar_child.getText().toString());

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
        bloodgrp = position -1;
    }
    private void Listnerforbloodgroup()
    {
        Blood_group = findViewById(R.id.create_referral_blood_group);
        Blood_group.setOnItemSelectedListener(this);

    }
    /*@Override
    public void Onblood(AdapterView<?> parent, View view, int position, long id) {
        bloodgrp = position - 1;
    }*/


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}