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
    private static final String TAG = "CreateReferralActivity";

    private int mYear, mMonth, mDay;
    private Button btn_submit;
    private EditText et_parent_name, et_child_f_name, et_child_l_name, et_bloodgp,
            et_ashamsmt, et_pincode, et_height, et_symptoms,
            et_weight, et_aadhaar_parent, et_aadhaar_child, et_phone, et_village, et_tehsil, et_district, et_treatedFor;
    private RadioButton rb_child_male, rb_child_female;
    private int day_of_birth, month_of_birth, year_of_birth;
    private Spinner spinner_oedema;
    private Spinner sp_state;
    public FirebaseFirestore db;
    ArrayList<String> states = new ArrayList<>();
    private int oedema_stage = -1;
    Referral referral = null;
    private Button btn_pickDate;
    private TextView tv_dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_referral);

        btn_submit = findViewById(R.id.create_referral_btn_submit);
        et_parent_name = findViewById(R.id.create_referral_et_parent_name);
        et_child_f_name = findViewById(R.id.create_referral_et_child_f_name);
        et_child_l_name = findViewById(R.id.create_referral_et_child_l_name);
        et_bloodgp = findViewById(R.id.create_referral_et_bloodgp);
        sp_state = findViewById(R.id.create_referral_et_state);
        et_ashamsmt = findViewById(R.id.create_referral_et_ashamsmt);
        et_pincode = findViewById(R.id.create_referral_et_pin);
        et_symptoms = findViewById(R.id.create_referral_et_symptoms);
        et_height = findViewById(R.id.create_referral_et_height);
        et_weight = findViewById(R.id.create_referral_et_weight);
        et_aadhaar_parent = findViewById(R.id.create_referral_et_aadhaar_parent);
        et_aadhaar_child = findViewById(R.id.create_referral_et_aadhaar_child);
        et_phone = findViewById(R.id.create_referral_et_phone);
        rb_child_male = findViewById(R.id.create_referral_rb_child_male);
        rb_child_female = findViewById(R.id.create_referral_rb_child_female);
        spinner_oedema = findViewById(R.id.create_referral_spinner_oedema);
        et_tehsil = findViewById(R.id.create_referral_et_tehsil);
        et_village = findViewById(R.id.create_referral_et_village);
        et_district = findViewById(R.id.create_referral_et_district);
        et_treatedFor = findViewById(R.id.create_referral_et_treatedfor);
        btn_pickDate = findViewById(R.id.pick_date);
        tv_dob = findViewById(R.id.date);

        referral = (Referral) getIntent().getSerializableExtra("referral");
        if (referral != null) {
            et_height.setText(String.valueOf((int) referral.getHeight()));
            et_weight.setText(String.valueOf(referral.getWeight()));
            et_ashamsmt.setText(String.valueOf((int) referral.getAsha_measure()));
            month_of_birth = referral.getMonth_of_birth();
            day_of_birth = referral.getDay_of_birth();
            year_of_birth = referral.getYear_of_birth();
            tv_dob.setText(day_of_birth + "-" + month_of_birth + "-" + year_of_birth);
            oedema_stage = referral.getOedema();
            spinner_oedema.setSelection(oedema_stage + 1);

            if (referral.getChild_gender().equals("f")) {
                rb_child_female.toggle();
            } else {
                rb_child_male.toggle();
            }
        } else {
            referral = new Referral();
        }

        spinner_oedema.setOnItemSelectedListener(this);
        db = FirebaseFirestore.getInstance();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = null;
                if (rb_child_male.isChecked()) {
                    gender = "m";
                } else if (rb_child_female.isChecked()) {
                    gender = "f";
                } else {
                    Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                String aadhaarnum_p = et_aadhaar_parent.getText().toString();
                boolean correctaadhaarnumresult = Verhoeff.validateVerhoeff(aadhaarnum_p);
                if (correctaadhaarnumresult){
                    createNewReferral();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enter valid AADHAAR number of parent",Toast.LENGTH_LONG).show();
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
                tv_dob.setText(sdf.format(myCalendar.getTime()));
            }
        };

        btn_pickDate.setOnClickListener(new View.OnClickListener() {

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
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                if (year < mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear, mMonth, mDay);

                                tv_dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                day_of_birth = dayOfMonth + 1;
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

        referral.setDay_of_birth(day_of_birth);

        referral.setMonth_of_birth(month_of_birth);

        referral.setYear_of_birth(year_of_birth);

        referral.setBlood_group(et_bloodgp.getText().toString());

        referral.setAsha_measure(Integer.parseInt(et_ashamsmt.getText().toString()));

        referral.setHeight(Integer.parseInt(et_height.getText().toString()));

        referral.setWeight(Float.parseFloat(et_weight.getText().toString()));

        referral.setOedema(oedema_stage);

        referral.setGuadian_name(et_parent_name.getText().toString());

        referral.setGuardian_aadhaar_num(et_aadhaar_parent.getText().toString());

        referral.setNrc_id(null);

        referral.setRcr_id(userid);

        referral.setOther_symptoms(et_symptoms.getText().toString());

        referral.setPhone(et_phone.getText().toString());

        referral.setState(sp_state.getSelectedItem().toString());

        referral.setDistrict(et_district.getText().toString());

        referral.setVillage(et_village.getText().toString());

        referral.setTehsil(et_tehsil.getText().toString());

        referral.setTreated_for(et_treatedFor.getText().toString());

        referral.setPincode(Integer.parseInt(et_pincode.getText().toString()));

        referral.setChild_aadhaar_num(et_aadhaar_child.getText().toString());

        referral.setStatus("Created");


        newReferralRef.set(referral).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(CreateReferralActivity.this, RCRActivity.class);
                    startActivity(intent);
                    finish();
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