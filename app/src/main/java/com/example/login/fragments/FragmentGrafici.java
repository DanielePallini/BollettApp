package com.example.login.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class FragmentGrafici extends Fragment {

    private FirebaseAuth mAuth;
    private ArrayList<BollettaLGI> bollette;
    public ArrayList<Entry> entries;
    private String periodoRiferimento = "";
    //private FeedAdapter feedAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bollette = new ArrayList<>();
        entries = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

         */
        View view = inflater.inflate(R.layout.fragment_grafici, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.grafici);
        setHasOptionsMenu(true);




        ListView lv = view.findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<>();

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


    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {

            ChartItem ci = getItem(position);
            return ci != null ? ci.getItemType() : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }
    }


    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> entry = new ArrayList<>();
        ArrayList<Entry> entry2 = new ArrayList<>();
        ArrayList<Entry> entry3 = new ArrayList<>();
        ArrayList<Entry> entry4 = new ArrayList<>();

        int cntLuce = 0;
        int cntGas = 0;
        int cntInternet = 0;
        ArrayList<BollettaLGI> list= MainActivity.bollette;
        for (BollettaLGI data : list) {
            switch(data.getTipo()){
                case "Luce":
                    entry.add(new Entry(cntLuce, (float)data.getCosto()));
                    cntLuce++;
                    break;
                case "Gas":
                    entry2.add(new Entry(cntGas, (float)data.getCosto()));
                    cntGas++;
                    break;
                case "Internet":
                    entry3.add(new Entry(cntInternet, (float)data.getCosto()));
                    cntInternet++;
                    break;


            }


        }
        entry4.add(new Entry(0,0));
        LineDataSet d1 = new LineDataSet(entry, "Luce "  );
        Utils.init(getContext());
        d1.setLineWidth(4.5f);
        d1.setCircleRadius(4.5f);
        d1.setDrawValues(false);
        d1.setColor(ColorTemplate.MATERIAL_COLORS[1]);
        d1.setCircleColor(ColorTemplate.MATERIAL_COLORS[1]);

        LineDataSet d2 = new LineDataSet(entry2, "Gas " );
        d2.setLineWidth(4.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.MATERIAL_COLORS[2]);
        d2.setCircleColor(ColorTemplate.MATERIAL_COLORS[2]);
        d2.setDrawValues(false);

        LineDataSet d3 = new LineDataSet(entry3, "Internet " );
        d3.setLineWidth(4.5f);
        d3.setCircleRadius(4.5f);
        d3.setHighLightColor(Color.rgb(244, 117, 117));
        d3.setColor(ColorTemplate.MATERIAL_COLORS[3]);
        d3.setCircleColor(ColorTemplate.MATERIAL_COLORS[3]);
        d3.setDrawValues(false);
        LineDataSet d4 = new LineDataSet(entry4, "\t \t \t \t Grafico andamento costi " );
        d4.setColor(Color.WHITE);
        d4.setCircleColor(Color.WHITE);
        d4.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);
        sets.add(d3);
        sets.add(d4);
        return new LineData(sets);
    }


    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<BollettaLGI> list = MainActivity.bollette;
        String tmp = "";
        Utils.init(getContext());
        double Mesi[] = new double[12];
        for (BollettaLGI data : list) {
            tmp = data.getPeriodo();
            tmp = tmp.substring(3,5);

            switch (tmp) {
                case "01":
                    Mesi[0] += data.getCosto();
                    break;
                case "02":
                    Mesi[1] += data.getCosto();
                    break;
                case "03":
                    Mesi[2] += data.getCosto();
                    break;
                case "04":
                    Mesi[3] += data.getCosto();
                    break;
                case "05":
                    Mesi[4] += data.getCosto();
                    break;
                case "06":
                    Mesi[5] += data.getCosto();
                    break;
                case "07":
                    Mesi[6] += data.getCosto();
                    break;
                case "08":
                    Mesi[7] += data.getCosto();
                    break;
                case "09":
                    Mesi[8] += data.getCosto();
                    break;
                case "10":
                    Mesi[9] += data.getCosto();
                    break;
                case "11":
                    Mesi[10] += data.getCosto();
                    break;
                case "12":
                    Mesi[11] += data.getCosto();
                    break;
            }
            for (int i = 0; i < 12; i++) {
                entries.add(new BarEntry((float) i+1,(float) Mesi[i]));
            }
        }
        BarDataSet d = new BarDataSet(entries, "Somma mensile dei costi ");
        d.setColors(ColorTemplate.MATERIAL_COLORS[1],ColorTemplate.MATERIAL_COLORS[2],ColorTemplate.MATERIAL_COLORS[3]);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }


    private PieData generateDataPie() {
        ArrayList<BollettaLGI> list= MainActivity.bollette;
        ArrayList<PieEntry> entries = new ArrayList<>();
        double sommaLuce = 0.0;
        double sommaGas = 0.0;
        double sommaInternet = 0.0;
        String tmp = "";
        Utils.init(getContext());

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int start3 = month - 3;
        if (start3 <= 0) start3 = start3 + 12;
        int start6 = month - 6;
        if (start6 <= 0) start6 = start6 + 6;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int currentYear = cal.get(Calendar.YEAR) -2000;
        int lastYear = currentYear - 1;
        int finePeriodo;
        int anno;


        for (BollettaLGI data : list) {
            tmp = data.getFinePeriodo();
             finePeriodo = Integer.parseInt(tmp.substring(3,5));
             anno = Integer.parseInt(tmp.substring(6,8));

            if(data.getTipo() == "Luce"){
                switch (periodoRiferimento){
                    case "3 mesi":
                        if ((month-3 >= 0) && (anno == currentYear) && (month-3 <= finePeriodo) && (finePeriodo <= month) ){
                            sommaLuce += data.getCosto();
                            break;
                        }
                        else if ((month-3 <= 0) && (start3 <= finePeriodo) && (anno == lastYear)){
                        sommaLuce += data.getCosto();
                        }
                        break;
                    case "6 mesi":
                        if ((month-6 >= 0) && (anno == currentYear) && (month-6 <= finePeriodo) && (finePeriodo <= month)){
                            sommaLuce += data.getCosto();
                            break;
                        }
                        else if ((month-6 <= 0) && (start6 <= finePeriodo) && (anno == lastYear)){
                            sommaLuce += data.getCosto();
                        }
                        break;
                    case "1 anno":
                        if ((anno == currentYear) && (finePeriodo <= month)){
                            sommaLuce += data.getCosto();
                            break;
                        }
                        else if ((anno == lastYear) && (month <= finePeriodo)){
                            sommaLuce += data.getCosto();
                        }
                        break;
                    default:
                        sommaLuce += data.getCosto();
                        break;

                }

            }
            if(data.getTipo() == "Gas"){
                switch (periodoRiferimento){
                    case "3 mesi":
                        if ((month-3 >= 0) && (anno == currentYear) && (month-3 <= finePeriodo) && (finePeriodo <= month) ){
                            sommaGas += data.getCosto();
                            break;
                        }
                        else if ((month-3 <= 0) && (start3 <= finePeriodo) && (anno == lastYear)){
                            sommaGas += data.getCosto();
                        }
                        break;
                    case "6 mesi":
                    if ((month-6 >= 0) && (anno == currentYear) && (month-6 <= finePeriodo) && (finePeriodo <= month)){
                        sommaGas += data.getCosto();
                        break;
                    }
                    else if ((month-6 <= 0) && (start6 <= finePeriodo) && (anno == lastYear)){
                        sommaGas += data.getCosto();
                    }
                    break;
                    case "1 anno":
                        if ((anno == currentYear) && (finePeriodo <= month)){
                            sommaGas += data.getCosto();
                            break;
                        }
                        else if ((anno == lastYear) && (month <= finePeriodo)){
                            sommaGas += data.getCosto();
                        }
                        break;
                    default:
                        sommaGas += data.getCosto();
                        break;
                }

            }
            if(data.getTipo() == "Internet"){
                switch (periodoRiferimento){
                    case "3 mesi":
                        if ((month-3 >= 0) && (anno == currentYear) && (month-3 <= finePeriodo) && (finePeriodo <= month) ){
                            sommaInternet += data.getCosto();
                            break;
                        }
                        else if ((month-3 <= 0) && (start3 <= finePeriodo) && (anno == lastYear)){
                            sommaInternet += data.getCosto();
                        }
                        break;
                    case "6 mesi":
                        if ((month-6 >= 0) && (anno == currentYear) && (month-6 <= finePeriodo) && (finePeriodo <= month)){
                            sommaInternet += data.getCosto();
                            break;
                        }
                        else if ((month-6 <= 0) && (start6 <= finePeriodo) && (anno == lastYear)){
                            sommaInternet += data.getCosto();
                        }
                        break;
                    case "1 anno":
                        if ((anno == currentYear) && (finePeriodo <= month)){
                            sommaInternet += data.getCosto();
                            break;
                        }
                        else if ((anno == lastYear) && (month <= finePeriodo)){
                            sommaInternet += data.getCosto();
                        }
                        break;
                    default:
                        sommaInternet += data.getCosto();
                        break;

                }
                //sommaInternet += data.getCosto();
            }

        }
        Log.d(TAG, "generateDataPie: " + sommaLuce);
        Log.d(TAG, "generateDataPie: " + sommaGas);
        Log.d(TAG, "generateDataPie: " + sommaInternet);
        entries.add(new PieEntry((float) sommaLuce, "Luce " ));
        entries.add(new PieEntry((float) sommaGas, "Gas "));
        entries.add(new PieEntry((float) sommaInternet, "Internet"));



        PieDataSet d = new PieDataSet(entries, "");


        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.MATERIAL_COLORS[1], ColorTemplate.MATERIAL_COLORS[2],ColorTemplate.MATERIAL_COLORS[3]);

        return new PieData(d);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.data_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_3mesi:
                Log.d(TAG, "onOptionsItemSelected: 3mesi");
                periodoRiferimento = "3 mesi";
                break;

            case R.id.menu_6mesi:
                Log.d(TAG, "onOptionsItemSelected: 6mesi");
                periodoRiferimento = "6 mesi";
                break;
            case R.id.menu_1anno:
                Log.d(TAG, "onOptionsItemSelected: 1anno");
                periodoRiferimento = "1 anno";
                break;
            case R.id.menu_tutti:
                Log.d(TAG, "onOptionsItemSelected: tutti");
                periodoRiferimento = "tutti";
                break;
        }
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
        return true;

    }



}
