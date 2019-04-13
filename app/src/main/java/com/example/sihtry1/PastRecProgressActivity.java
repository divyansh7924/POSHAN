package com.example.sihtry1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

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
import java.util.List;

public class PastRecProgressActivity extends Activity {
    String selItem = null;
    private PastRecord pastRecord = null;
    AnyChartView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_rec_progress);

        selItem = getIntent().getStringExtra("selitem");
        pastRecord = (PastRecord) getIntent().getSerializableExtra("PastRecord");
        graph = findViewById(R.id.past_rec_progress_graph);


        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        Log.v("sel", selItem);
        ArrayList progdata = new ArrayList();
        List<DataEntry> seriesData = new ArrayList<>();
        int n;
        int i = 0;
        switch (selItem) {
            case "Height":
                cartesian.title("Height Progress of the child");
                cartesian.yAxis(0).title("Height of child");
                seriesData.add(new ValueDataEntry("Admitted", pastRecord.getAdmit_height()));
                seriesData.add(new ValueDataEntry("Discharged", pastRecord.getDisch_height()));
                progdata = pastRecord.getFllw_height();
                for (n = progdata.size(); i < n; i++) {
                    seriesData.add(new ValueDataEntry("Followup" + i, (Number) progdata.get(i)));
                }
                break;
            case "Weight":
                cartesian.title("Weight Progress of the child");
                cartesian.yAxis(0).title("Weight of child");
                progdata = pastRecord.getFllw_weight();
                for (n = progdata.size(); i < n; i++) {
                    seriesData.add(new ValueDataEntry("Followup" + i, (Number) progdata.get(i)));
                }
                break;
            case "Oedema":
                cartesian.title("Oedema Progress of the child");
                cartesian.yAxis(0).title("Oedema of child");
                seriesData.add(new ValueDataEntry("Admitted", pastRecord.getAdmit_oedema()));
                seriesData.add(new ValueDataEntry("Discharged", pastRecord.getDisch_oedema()));
                progdata = pastRecord.getFllw_oedema();
                for (n = progdata.size(); i < n; i++) {
                    seriesData.add(new ValueDataEntry("Followup" + i, (Number) progdata.get(i)));
                }
                break;
            case "MUAC":
                cartesian.title("MUAC Progress of the child");
                cartesian.yAxis(0).title("MUAC of child");
                seriesData.add(new ValueDataEntry("Admitted", pastRecord.getAdmit_asha_measure()));
                seriesData.add(new ValueDataEntry("Discharged", pastRecord.getDisch_asha_measure()));
                progdata = pastRecord.getFllw_asha_measure();
                for (n = progdata.size(); i < n; i++) {
                    seriesData.add(new ValueDataEntry("Followup" + i, (Number) progdata.get(i)));
                }
        }


        cartesian.xAxis(0).labels().padding(2d, 2d, 2d, 2d);


        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        switch (selItem) {
            case "Height":
                series1.name("Height");
                break;
            case "Weight":
                series1.name("Weight");
                break;
            case "Oedema":
                series1.name("Oedema");
                break;
            case "MUAC":
                series1.name("MUAC");
        }

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
}
