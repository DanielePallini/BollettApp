package com.example.login.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FragmentBolletta extends Fragment {
    private TextInputEditText textDataScadenza, textPeriodo, textCosto, textConsumo;
    private TextView euro, kwh;
    private Button btnSalva;
    private int num = 0;
    final Calendar myCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bolletta, container, false);
        textDataScadenza = view.findViewById(R.id.text_data_scadenza);
        textPeriodo = view.findViewById(R.id.text_periodo_riferimento);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(num);
            }

        };
        textDataScadenza.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                num = 1;
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        textPeriodo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                num = 2;
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        textCosto = view.findViewById(R.id.text_costo);
        euro = view.findViewById(R.id.euro);
        textConsumo = view.findViewById(R.id.text_consumo);
        kwh = view.findViewById(R.id.misura);
        btnSalva = view.findViewById(R.id.btn_salva);
        return view;

    }
    private void updateLabel(int num) {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        switch (num){
            case 1:
                textDataScadenza.setText(sdf.format(myCalendar.getTime()));
                break;
            case 2:
                textPeriodo.setText(sdf.format(myCalendar.getTime()));
        }

    }
    /*
    @Override
    public void onClick(View v) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        switch (v.getId()){
            case R.id.text_data_scadenza:
                textDataScadenza.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(getActivity(), date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                break;


        }
        */

    }






