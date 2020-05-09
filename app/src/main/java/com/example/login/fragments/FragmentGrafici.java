package com.example.login.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.MainActivity;
import com.example.login.R;
import com.example.login.charts.BarChartItem;
import com.example.login.entities.BollettaLGI;
import com.example.login.charts.ChartItem;
import com.example.login.charts.LineChartItem;
import com.example.login.charts.PieChartItem;
import com.example.login.uiutilities.FeedAdapter;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FragmentGrafici extends Fragment {

    private FirebaseAuth mAuth;
    private ArrayList<BollettaLGI> bollette;
    public ArrayList<Entry> entries;
    private FeedAdapter feedAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bollette = new ArrayList<>();
        entries = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View view = inflater.inflate(R.layout.fragment_grafici, container, false);

        //BarChart chart = view.findViewById(R.id.barchart);
        /*
        LineChart chart = (LineChart) view.findViewById(R.id.chart);
        ArrayList<BollettaLuce> list= MainActivity.bollette;
        for (BollettaLuce data : list) {

            // turn your data into Entry objects
            entries.add(new Entry((float)data.getConsumo(), (float)data.getCosto()));

        }
        //}
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(R.color.colorAccent);
        dataSet.setValueTextColor(R.color.colorAccent);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh

        */





        getActivity().setTitle("ListViewMultiChartActivity");

        ListView lv = view.findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<>();

        // 30 items
        for (int i = 0; i < 3; i++) {

            if(i % 3 == 0) {
                list.add(new LineChartItem(generateDataLine(i + 1), getActivity().getApplicationContext()));
            } else if(i % 3 == 1) {
                list.add(new BarChartItem(generateDataBar(i + 1), getActivity().getApplicationContext()));
            } else if(i % 3 == 2) {
                list.add(new PieChartItem(generateDataPie(), getActivity().getApplicationContext()));
            }
        }

        ChartDataAdapter cda = new ChartDataAdapter(getActivity().getApplicationContext(), list);
        lv.setAdapter(cda);
        return view;
    }

    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //noinspection ConstantConditions
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            ChartItem ci = getItem(position);
            return ci != null ? ci.getItemType() : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Line data
     */
    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> entries = new ArrayList<>();
        /*
        for (int i = 0; i < 12; i++) {
            values1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

         */
        ArrayList<BollettaLGI> list= MainActivity.bollette;
        for (BollettaLGI data : list) {

            // turn your data into Entry objects
            entries.add(new Entry((float)data.getConsumo(), (float)data.getCosto()));

        }

        LineDataSet d1 = new LineDataSet(entries, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        /*
        ArrayList<Entry> values2 = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            values2.add(new Entry(i, values1.get(i).getY() - 30));
        }

        LineDataSet d2 = new LineDataSet(values2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);
        */
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        //sets.add(d2);

        return new LineData(sets);
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Bar data
     */
    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<BollettaLGI> list= MainActivity.bollette;
        for (BollettaLGI data : list) {

            // turn your data into Entry objects
            entries.add(new BarEntry((float)data.getId(), (float)data.getCosto()));

        }

        //for (int i = 0; i < 12; i++) {
          //  entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        //}

        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Pie data
     */
    private PieData generateDataPie() {
        ArrayList<BollettaLGI> list= MainActivity.bollette;
        ArrayList<PieEntry> entries = new ArrayList<>();
        double sommaLuce = 0.0;
        double sommaGas = 0.0;
        double sommaInternet = 0.0;
        /*
        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i+1)));
        }

         */
        for (BollettaLGI data : list) {
            if(data.getTipo() == "Luce"){
                sommaLuce += data.getCosto();
            }
            if(data.getTipo() == "Gas"){
                sommaGas += data.getCosto();
            }
            if(data.getTipo() == "Internet"){
                sommaInternet += data.getCosto();
            }

        }
        entries.add(new PieEntry((float) sommaLuce, "Luce " ));
        entries.add(new PieEntry((float) sommaGas, "Gas "));
        entries.add(new PieEntry((float) sommaInternet, "Internet"));



        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.MATERIAL_COLORS);

        return new PieData(d);
    }




}
