package com.example.sihtry1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.example.sihtry1.models.PastRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PastRecordInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private PastRecord pastRecord = null;
    private TextView tv_guardianname, tv_childname, tv_gender, tv_dob, tv_bloodgrp, tv_muac, tv_height,
            tv_weight, tv_oedema, tv_treatedfor;
    AnyChartView graph;
    Spinner pastrecsel;
    String selitem;

    private static final String TAG = "PastRecordInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pastRecord = (PastRecord) getIntent().getSerializableExtra("PastRecord");
        setContentView(R.layout.activity_past_record_info);

        pastRecord = (PastRecord) getIntent().getSerializableExtra("PastRecord");
        tv_guardianname = findViewById(R.id.past_rec_info_tv_guardian_name);
        tv_childname = findViewById(R.id.past_rec_info_tv_child_name);
        tv_gender = findViewById(R.id.past_rec_info_tv_child_gender);
        tv_dob = findViewById(R.id.past_rec_info_tv_date_of_birth);
        tv_bloodgrp = findViewById(R.id.past_rec_info_tv_blood_group);
        tv_muac = findViewById(R.id.past_rec_info_tv_asha_tape);
        tv_height = findViewById(R.id.past_rec_info_tv_height);
        tv_weight = findViewById(R.id.past_rec_info_tv_weight);
        tv_oedema = findViewById(R.id.past_rec_info_tv_oedema);
        tv_treatedfor = findViewById(R.id.past_rec_info_tv_treated_for);
        graph = findViewById(R.id.past_rec_info_graph);
        pastrecsel = findViewById(R.id.past_rec_info_sel);

        tv_guardianname.setText(pastRecord.getGuardian_name());
        tv_childname.setText(pastRecord.getChild_first_name() + " " + pastRecord.getChild_last_name());

        if (pastRecord.getChild_gender() == "f") {
            tv_gender.setText("Female");
        } else {
            tv_gender.setText("Male");
        }

        tv_dob.setText(pastRecord.getDay_of_birth() + "/" + pastRecord.getMonth_of_birth() + "/" + pastRecord.getYear_of_birth());
        tv_bloodgrp.setText(pastRecord.getBlood_group());
        tv_muac.setText(String.valueOf(pastRecord.getDisch_asha_measure()));
        tv_height.setText(String.valueOf(pastRecord.getDisch_height()));
        tv_weight.setText(String.valueOf(pastRecord.getDisch_weight()));

        if (pastRecord.getDisch_oedema() == 0) {
            tv_oedema.setText("0");
        } else if (pastRecord.getDisch_oedema() == 1) {
            tv_oedema.setText("+");
        } else if (pastRecord.getDisch_oedema() == 2) {
            tv_oedema.setText("++");
        } else {
            tv_oedema.setText("+++");
        }

        tv_treatedfor.setText(pastRecord.getDisch_treated_for());
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.title("Height Progress of the child");
        cartesian.yAxis(0).title("Height of child");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pastselect, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pastrecsel.setAdapter(adapter);
        pastrecsel.setOnItemSelectedListener(this);


        String recsel = pastrecsel.getOnItemSelectedListener().toString();
        Log.v(TAG, recsel);

        ArrayList<Integer> height = new ArrayList();
        height = pastRecord.getFllw_height();
        ArrayList<Date> followupdates = new ArrayList();
        followupdates = pastRecord.getFollowup_dates();
//        ArrayList<String> followupdatesstring = new ArrayList<>();
        List<DataEntry> seriesData = new ArrayList<>();

        int n;
        int i = 0;
        for (n = height.size(); i < n; i++) {
//            followupdatesstring.add(followupdates.get(i).toString());
            Log.v(TAG, "mbmbmbm");
            seriesData.add(new ValueDataEntry(followupdates.get(i).toString(), height.get(i)));
        }


        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Height");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);


        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        graph.setChart(cartesian);


    }
//    private class CustomDataEntry extends ValueDataEntry {
//
//        CustomDataEntry(String x, Number value, Number value2, Number value3) {
//            super(x, value);
//            setValue("value2", value2);
//            setValue("value3", value3);
//        }
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selitem = pastrecsel.getSelectedItem().toString();
        Log.v("selitem",selitem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
