package com.example.login.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
    private String argomentoGrafici = "Costi";
    private MenuItem itemConsumi;
    private MenuItem itemCosti;

    private boolean resultValue;

    Calendar cal = Calendar.getInstance();
    private int month = cal.get(Calendar.MONTH) + 1;
    private int start3 = month - 3;
    private int start6 = month - 6;


    private int currentYear = cal.get(Calendar.YEAR) -2000;
    private int lastYear = currentYear - 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bollette = new ArrayList<>();
        entries = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grafici, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Grafici " + argomentoGrafici + " " + periodoRiferimento);
        setHasOptionsMenu(true);
        if (start3 <= 0) start3 = start3 + 12;
        if (start6 <= 0) start6 = start6 + 12;


        ListView lv = view.findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {

            if(i % 3 == 0) {
                list.add(new LineChartItem(generateDataLine(i + 1), getActivity().getApplicationContext()));
            } else if(i % 3 == 1) {
                list.add(new BarChartItem(generateDataBar(i + 1), getActivity().getApplicationContext()));
            } else if(i % 3 == 2) {
                list.add(new PieChartItem(generateDataPie(), getActivity().getApplicationContext(), argomentoGrafici));
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

        String tmp = "";
        int cntLuce = 0;
        int cntGas = 0;
        int cntInternet = 0;
        int finePeriodo;
        int anno;

        ArrayList<BollettaLGI> list= MainActivity.bollette;
        for (BollettaLGI data : list) {

            tmp = data.getFinePeriodo();
            finePeriodo = Integer.parseInt(tmp.substring(3,5));
            anno = Integer.parseInt(tmp.substring(6,8));

            if(data.getTipo().equals("Luce")){
                switch (periodoRiferimento){
                    case "Ultimi 3 Mesi":
                        if ((month-3 >= 0) && (anno == currentYear) && (month-3 <= finePeriodo) && (finePeriodo <= month) ){

                            if (argomentoGrafici.equals("Consumi")) {
                                entry.add(new Entry(cntLuce, (float) data.getConsumo()));
                            } else {
                                entry.add(new Entry(cntLuce, (float) data.getCosto()));
                            }
                            cntLuce++;

                            break;
                        }
                        else if ((month-3 <= 0) && (anno == currentYear) && (finePeriodo <= month) ){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry.add(new Entry(cntLuce, (float) data.getConsumo()));
                            } else {
                                entry.add(new Entry(cntLuce, (float) data.getCosto()));
                            }
                            cntLuce++;

                            break;
                        }
                        else if ((month-3 <= 0) && (start3 <= finePeriodo) && (anno == lastYear)){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry.add(new Entry(cntLuce, (float) data.getConsumo()));
                            } else {
                                entry.add(new Entry(cntLuce, (float) data.getCosto()));
                            }
                            cntLuce++;
                        }
                        break;
                    case "Ultimi 6 Mesi":
                        if ((month-6 >= 0) && (anno == currentYear) && (month-6 <= finePeriodo) && (finePeriodo <= month)){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry.add(new Entry(cntLuce, (float) data.getConsumo()));
                            } else {
                                entry.add(new Entry(cntLuce, (float) data.getCosto()));
                            }
                            cntLuce++;
                            break;
                        }

                        else if ((month-6 <= 0) && (anno == currentYear) && (finePeriodo <= month) ){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry.add(new Entry(cntLuce, (float) data.getConsumo()));
                            } else {
                                entry.add(new Entry(cntLuce, (float) data.getCosto()));
                            }
                            cntLuce++;

                            break;
                        }
                        else if ((month-6 <= 0) && (start6 <= finePeriodo) && (anno == lastYear)) {
                            if (argomentoGrafici.equals("Consumi")) {
                                entry.add(new Entry(cntLuce, (float) data.getConsumo()));
                            } else {
                                entry.add(new Entry(cntLuce, (float) data.getCosto()));
                            }
                            cntLuce++;

                        }
                        break;
                    case "Ultimo Anno":
                        if ((anno == currentYear) && (finePeriodo <= month)){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry.add(new Entry(cntLuce, (float) data.getConsumo()));
                            } else {
                                entry.add(new Entry(cntLuce, (float) data.getCosto()));
                            }
                            cntLuce++;
                            break;
                        }
                        else if ((anno == lastYear) && (month <= finePeriodo)) {
                            if (argomentoGrafici.equals("Consumi")) {
                                entry.add(new Entry(cntLuce, (float) data.getConsumo()));
                            } else {
                                entry.add(new Entry(cntLuce, (float) data.getCosto()));
                            }
                            cntLuce++;

                        }
                        break;
                    default:
                        if (argomentoGrafici.equals("Consumi")) {
                            entry.add(new Entry(cntLuce, (float) data.getConsumo()));
                        } else {
                            entry.add(new Entry(cntLuce, (float) data.getCosto()));
                        }
                        cntLuce++;
                        break;

                }

            }
            if(data.getTipo().equals("Gas")){
                switch (periodoRiferimento){
                    case "Ultimi 3 Mesi":
                        if ((month-3 >= 0) && (anno == currentYear) && (month-3 <= finePeriodo) && (finePeriodo <= month) ){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry2.add(new Entry(cntGas, (float) data.getConsumo()));
                            } else {
                                entry2.add(new Entry(cntGas, (float) data.getCosto()));
                            }
                            cntGas++;
                            break;
                        }
                        else if ((month-3 <= 0) && (anno == currentYear)&& (finePeriodo <= month) ){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry2.add(new Entry(cntGas, (float) data.getConsumo()));
                            } else {
                                entry2.add(new Entry(cntGas, (float) data.getCosto()));
                            }
                            cntGas++;

                            break;
                        }
                        else if ((month-3 <= 0) && (start3 <= finePeriodo) && (anno == lastYear)){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry2.add(new Entry(cntGas, (float) data.getConsumo()));
                            } else {
                                entry2.add(new Entry(cntGas, (float) data.getCosto()));
                            }
                            cntGas++;

                        }
                        break;
                    case "Ultimi 6 Mesi":
                        if ((month-6 >= 0) && (anno == currentYear) && (month-6 <= finePeriodo) && (finePeriodo <= month)){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry2.add(new Entry(cntGas, (float) data.getConsumo()));
                            } else {
                                entry2.add(new Entry(cntGas, (float) data.getCosto()));
                            }
                            cntGas++;
                            break;
                        }
                        else if ((month-6 <= 0) && (anno == currentYear) && (finePeriodo <= month)){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry2.add(new Entry(cntGas, (float) data.getConsumo()));
                            } else {
                                entry2.add(new Entry(cntGas, (float) data.getCosto()));
                            }
                            cntGas++;
                            break;
                        }
                        else if ((month-6 <= 0) && (start6 <= finePeriodo) && (anno == lastYear)){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry2.add(new Entry(cntGas, (float) data.getConsumo()));
                            } else {
                                entry2.add(new Entry(cntGas, (float) data.getCosto()));
                            }
                            cntGas++;

                        }
                        break;
                    case "Ultimo Anno":
                        if ((anno == currentYear) && (finePeriodo <= month)){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry2.add(new Entry(cntGas, (float) data.getConsumo()));
                            } else {
                                entry2.add(new Entry(cntGas, (float) data.getCosto()));
                            }
                            cntGas++;
                            break;
                        }
                        else if ((anno == lastYear) && (month <= finePeriodo)){
                            if (argomentoGrafici.equals("Consumi")) {
                                entry2.add(new Entry(cntGas, (float) data.getConsumo()));
                            } else {
                                entry2.add(new Entry(cntGas, (float) data.getCosto()));
                            }
                            cntGas++;

                        }
                        break;
                    default:
                        if (argomentoGrafici.equals("Consumi")) {
                            entry2.add(new Entry(cntGas, (float) data.getConsumo()));
                        } else {
                            entry2.add(new Entry(cntGas, (float) data.getCosto()));
                        }
                        cntGas++;
                        break;

                }

            }
            if(data.getTipo().equals("Internet")){
                if (!argomentoGrafici.equals("Consumi")) {
                    switch (periodoRiferimento) {
                        case "Ultimi 3 Mesi":
                            if ((month - 3 >= 0) && (anno == currentYear) && (month - 3 <= finePeriodo) && (finePeriodo <= month)) {
                                entry3.add(new Entry(cntInternet, (float) data.getCosto()));
                                cntInternet++;
                                break;
                            }
                            else if ((month - 3 <= 0) && (anno == currentYear) && (finePeriodo <= month)) {
                                entry3.add(new Entry(cntInternet, (float) data.getCosto()));
                                cntInternet++;
                                break;
                            }

                            else if ((month - 3 <= 0) && (start3 <= finePeriodo) && (anno == lastYear)) {
                                entry3.add(new Entry(cntInternet, (float) data.getCosto()));
                                cntInternet++;
                            }
                            break;
                        case "Ultimi 6 Mesi":
                            if ((month - 6 >= 0) && (anno == currentYear) && (month - 6 <= finePeriodo) && (finePeriodo <= month)) {
                                entry3.add(new Entry(cntInternet, (float) data.getCosto()));
                                cntInternet++;
                                break;
                            }
                            else if ((month - 6 <= 0) && (anno == currentYear) && (finePeriodo <= month)) {
                                entry3.add(new Entry(cntInternet, (float) data.getCosto()));
                                cntInternet++;
                                break;
                            }
                            else if ((month - 6 <= 0) && (start6 <= finePeriodo) && (anno == lastYear)) {
                                entry3.add(new Entry(cntInternet, (float) data.getCosto()));
                                cntInternet++;
                            }
                            break;
                        case "Ultimo Anno":
                            if ((anno == currentYear) && (finePeriodo <= month)) {
                                entry3.add(new Entry(cntInternet, (float) data.getCosto()));
                                cntInternet++;
                                break;
                            } else if ((anno == lastYear) && (month <= finePeriodo)) {
                                entry3.add(new Entry(cntInternet, (float) data.getCosto()));
                                cntInternet++;
                            }
                            break;
                        default:
                            entry3.add(new Entry(cntInternet, (float) data.getCosto()));
                            cntInternet++;
                            break;

                    }
                }

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
        LineDataSet d4;
        if (argomentoGrafici.equals("Consumi")){
            d4 = new LineDataSet(entry4, "\t \t \t \t \t  Grafico andamento consumi " );
        } else {
            d4 = new LineDataSet(entry4, "\t \t  Grafico andamento costi ");
        }
        d4.setColor(Color.WHITE);
        d4.setCircleColor(Color.WHITE);
        d4.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        if ((cntLuce > 1) || (cntGas >1) || (cntInternet > 1))   {
            sets.add(d1);
            sets.add(d2);
            if (!argomentoGrafici.equals("Consumi")) {
                sets.add(d3);
            }
        }

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
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[0] += data.getConsumo();
                        }
                    } else {
                        Mesi[0] += data.getCosto();
                    }
                    break;
                case "02":
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[1] += data.getConsumo();
                        }
                    } else {
                        Mesi[1] += data.getCosto();
                    }
                    break;
                case "03":
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[2] += data.getConsumo();
                        }
                    } else {
                        Mesi[2] += data.getCosto();
                    }
                    break;
                case "04":
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[3] += data.getConsumo();
                        }
                    } else {
                        Mesi[3] += data.getCosto();
                    }
                    break;
                case "05":
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[4] += data.getConsumo();
                        }
                    } else {
                        Mesi[4] += data.getCosto();
                    }
                    break;
                case "06":
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[5] += data.getConsumo();
                        }
                    } else {
                        Mesi[5] += data.getCosto();
                    }
                    break;
                case "07":
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[6] += data.getConsumo();
                        }
                    } else {
                        Mesi[6] += data.getCosto();
                    }
                    break;
                case "08":
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[7] += data.getConsumo();
                        }
                    } else {
                        Mesi[7] += data.getCosto();
                    }
                    break;
                case "09":
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[8] += data.getConsumo();
                        }
                    } else {
                        Mesi[8] += data.getCosto();
                    }
                    break;
                case "10":
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[9] += data.getConsumo();
                        }
                    } else {
                        Mesi[9] += data.getCosto();
                    }
                    break;
                case "11":
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[10] += data.getConsumo();
                        }
                    } else {
                        Mesi[10] += data.getCosto();
                    }
                    break;
                case "12":
                    if (argomentoGrafici.equals("Consumi")){
                        if (data.getTipo().equals("Internet")) {
                            break;
                        } else {
                            Mesi[11] += data.getConsumo();
                        }
                    } else {
                        Mesi[11] += data.getCosto();
                    }
                    break;
            }

            switch (periodoRiferimento){
                case "Ultimi 3 Mesi":
                    for (int i = month-3; i<= month; i++) {
                        if (i <= 0) {
                            entries.add(new BarEntry((float) i+12,(float) Mesi[i+11]));
                        } else {
                            entries.add(new BarEntry((float) i, (float) Mesi[i - 1]));
                        }
                    }
                    break;
                case "Ultimi 6 Mesi":
                    for (int i = month-6; i<= month; i++) {
                        if (i <= 0) {
                            entries.add(new BarEntry((float) i+12,(float) Mesi[i+11]));
                        } else {
                            entries.add(new BarEntry((float) i, (float) Mesi[i - 1]));
                        }
                    }
                    break;

                default:
                    for (int i = 0; i < 12; i++) {
                        entries.add(new BarEntry((float) i+1,(float) Mesi[i]));
                    }
                    break;
            }

        }
        BarDataSet d;
        if (argomentoGrafici.equals("Consumi")){
            d = new BarDataSet(entries, "Somma mensile dei consumi ");

            d.setColor(ColorTemplate.MATERIAL_COLORS[3]);
        } else {
            d = new BarDataSet(entries, "Somma mensile dei costi ");
            d.setColor(ColorTemplate.MATERIAL_COLORS[0]);

        }


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


        int finePeriodo;
        int anno;


        for (BollettaLGI data : list) {
            tmp = data.getFinePeriodo();
             finePeriodo = Integer.parseInt(tmp.substring(3,5));
             anno = Integer.parseInt(tmp.substring(6,8));

            if(data.getTipo().equals("Luce")){
                switch (periodoRiferimento){
                    case "Ultimi 3 Mesi":
                        if ((month-3 >= 0) && (anno == currentYear) && (month-3 <= finePeriodo) && (finePeriodo <= month) ){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaLuce += data.getConsumo();
                            } else {
                                sommaLuce += data.getCosto();
                            }
                            break;
                        }
                        else if ((month-3 <= 0) && (anno == currentYear)&& (finePeriodo <= month) ){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaLuce += data.getConsumo();
                            } else {
                                sommaLuce += data.getCosto();
                            }
                        }
                        else if ((month-3 <= 0) && (start3 <= finePeriodo) && (anno == lastYear)){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaLuce += data.getConsumo();
                            } else {
                                sommaLuce += data.getCosto();
                            }
                        }
                        break;
                    case "Ultimi 6 Mesi":
                        if ((month-6 >= 0) && (anno == currentYear) && (month-6 <= finePeriodo) && (finePeriodo <= month)){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaLuce += data.getConsumo();
                            } else {
                                sommaLuce += data.getCosto();
                            }
                            break;
                        }
                        else if ((month-6 <= 0) && (anno == currentYear) && (finePeriodo <= month)){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaLuce += data.getConsumo();
                            } else {
                                sommaLuce += data.getCosto();
                            }
                        }
                        else if ((month-6 <= 0) && (start6 <= finePeriodo) && (anno == lastYear)){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaLuce += data.getConsumo();
                            } else {
                                sommaLuce += data.getCosto();
                            }
                        }
                        break;
                    case "Ultimo Anno":
                        if ((anno == currentYear) && (finePeriodo <= month)){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaLuce += data.getConsumo();
                            } else {
                                sommaLuce += data.getCosto();
                            }
                            break;
                        }

                        else if ((anno == lastYear) && (month <= finePeriodo)){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaLuce += data.getConsumo();
                            } else {
                                sommaLuce += data.getCosto();
                            }
                        }
                        break;
                    default:
                        if (argomentoGrafici.equals("Consumi")){
                            sommaLuce += data.getConsumo();
                        } else {
                            sommaLuce += data.getCosto();
                        }
                        break;

                }

            }
            if(data.getTipo().equals("Gas")){
                switch (periodoRiferimento){
                    case "Ultimi 3 Mesi":
                        if ((month-3 >= 0) && (anno == currentYear) && (month-3 <= finePeriodo) && (finePeriodo <= month) ){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaGas += data.getConsumo();
                            } else {
                                sommaGas += data.getCosto();
                            }
                            break;
                        }
                        else if ((month-3 <= 0) && (anno == currentYear) && (finePeriodo <= month)){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaGas += data.getConsumo();
                            } else {
                                sommaGas += data.getCosto();
                            }
                        }
                        else if ((month-3 <= 0) && (start3 <= finePeriodo) && (anno == lastYear)){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaGas += data.getConsumo();
                            } else {
                                sommaGas += data.getCosto();
                            }
                        }
                        break;
                    case "Ultimi 6 Mesi":
                    if ((month-6 >= 0) && (anno == currentYear) && (month-6 <= finePeriodo) && (finePeriodo <= month)){
                        if (argomentoGrafici.equals("Consumi")){
                            sommaGas += data.getConsumo();
                        } else {
                            sommaGas += data.getCosto();
                        }
                        break;
                    }
                    else if ((month-6 <= 0) && (anno == currentYear) && (finePeriodo <= month)){
                        if (argomentoGrafici.equals("Consumi")){
                            sommaGas += data.getConsumo();
                        } else {
                            sommaGas += data.getCosto();
                        }
                    }
                    else if ((month-6 <= 0) && (start6 <= finePeriodo) && (anno == lastYear)){
                        if (argomentoGrafici.equals("Consumi")){
                            sommaGas += data.getConsumo();
                        } else {
                            sommaGas += data.getCosto();
                        }
                    }
                    break;
                    case "Ultimo Anno":
                        if ((anno == currentYear) && (finePeriodo <= month)){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaGas += data.getConsumo();
                            } else {
                                sommaGas += data.getCosto();
                            }
                            break;
                        }
                        else if ((anno == lastYear) && (month <= finePeriodo)){
                            if (argomentoGrafici.equals("Consumi")){
                                sommaGas += data.getConsumo();
                            } else {
                                sommaGas += data.getCosto();
                            }
                        }
                        break;
                    default:
                        if (argomentoGrafici.equals("Consumi")){
                            sommaGas += data.getConsumo();
                        } else {
                            sommaGas += data.getCosto();
                        }
                        break;
                }

            }
            if(data.getTipo().equals("Internet")){
                if (!argomentoGrafici.equals("Consumi")) {
                    switch (periodoRiferimento) {
                        case "Ultimi 3 Mesi":
                            if ((month - 3 >= 0) && (anno == currentYear) && (month - 3 <= finePeriodo) && (finePeriodo <= month)) {
                                sommaInternet += data.getCosto();
                                break;
                            }
                            else if ((month-3 <= 0) && (anno == currentYear) && (finePeriodo <= month)){
                                sommaInternet += data.getCosto();
                            }
                            else if ((month - 3 <= 0) && (start3 <= finePeriodo) && (anno == lastYear)) {
                                sommaInternet += data.getCosto();
                            }
                            break;
                        case "Ultimi 6 Mesi":
                            if ((month - 6 >= 0) && (anno == currentYear) && (month - 6 <= finePeriodo) && (finePeriodo <= month)) {
                                sommaInternet += data.getCosto();
                                break;
                            }
                            else if ((month-6 <= 0) && (anno == currentYear) && (finePeriodo <= month)){
                                sommaInternet += data.getCosto();
                            }
                            else if ((month - 6 <= 0) && (start6 <= finePeriodo) && (anno == lastYear)) {

                                sommaInternet += data.getCosto();

                            }
                            break;
                        case "Ultimo Anno":
                            if ((anno == currentYear) && (finePeriodo <= month)) {
                                sommaInternet += data.getCosto();
                                break;
                            } else if ((anno == lastYear) && (month <= finePeriodo)) {
                                sommaInternet += data.getCosto();
                            }
                            break;
                        default:
                            sommaInternet += data.getCosto();
                            break;

                    }
                }

            }

        }



            entries.add(new PieEntry((float) sommaLuce, "Luce "));


            entries.add(new PieEntry((float) sommaGas, "Gas "));

        if (!argomentoGrafici.equals("Consumi")) {

                entries.add(new PieEntry((float) sommaInternet, "Internet"));

        }


        PieDataSet d = new PieDataSet(entries, "");


        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.MATERIAL_COLORS[1], ColorTemplate.MATERIAL_COLORS[2],ColorTemplate.MATERIAL_COLORS[3]);

        return new PieData(d);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.data_menu, menu);
        itemConsumi = menu.findItem(R.id.menu_consumi);
        itemCosti = menu.findItem(R.id.menu_costi);
        if (argomentoGrafici == "Consumi"){
            itemConsumi.setVisible(false);
            itemCosti.setVisible(true);
        } else {
            itemConsumi.setVisible(true);
            itemCosti.setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_consumi:

                boolean result = getDialogValueBack(getContext(), "Consumo");

                if (result){
                    argomentoGrafici = "Consumi";
                    break;
                } else {
                    return true;
                }

            case R.id.menu_costi:

                boolean result2 = getDialogValueBack(getContext(), "Costi");
                if (result2){
                    argomentoGrafici = "Costi";
                    break;
                } else {
                    return true;
                }
            case R.id.menu_3mesi:

                periodoRiferimento = "Ultimi 3 Mesi";
                break;
            case R.id.menu_6mesi:

                periodoRiferimento = "Ultimi 6 Mesi";
                break;
            case R.id.menu_1anno:

                periodoRiferimento = "Ultimo Anno";
                break;
            case R.id.menu_tutti:

                periodoRiferimento = "";
                break;
        }
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
        return true;

    }


    private boolean getDialogValueBack(Context context, String tipo)
    {
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                throw new RuntimeException();
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(true);
        if (tipo.equals("Consumo")){

            alert.setMessage(R.string.visualizzaconsumo);
        } else {
            alert.setMessage(R.string.visualizzacosto);
        }
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                resultValue = true;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                resultValue = false;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.show();

        try{ Looper.loop(); }
        catch(RuntimeException e){}

        return resultValue;

    }


}
