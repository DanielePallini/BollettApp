package com.example.login.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentBolletta extends Fragment {
    private TextInputEditText textDataScadenza, textPeriodo, textFine, textCosto, textConsumo;
    private TextInputLayout textInputLayout;
    private TextView euro, misura;
    private Button btnSalva;
    private int num = 0;
    final Calendar myCalendar = Calendar.getInstance();
    private FirebaseAuth mAuth;
    private FragmentFeed fragmentFeed;
    private long max = 0;
    String tipo = "";
    double consumo = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bolletta, container, false);
        textDataScadenza = view.findViewById(R.id.text_data_scadenza);
        textPeriodo = view.findViewById(R.id.text_periodo_riferimento);
        textFine = view.findViewById(R.id.text_fine_riferimento);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.inseriscidati));

        Bundle args = getArguments();
        max = args.getLong("max", 0);
        tipo = args.getString("tipo", "");
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
        textFine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                num = 3;
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        textCosto = view.findViewById(R.id.text_costo);
        euro = view.findViewById(R.id.euro);
        textConsumo = view.findViewById(R.id.text_consumo);
        textInputLayout = view.findViewById(R.id.layout_consumo);
        misura = view.findViewById(R.id.misura);
        btnSalva = view.findViewById(R.id.btn_salva);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        switch (tipo){
            case "Luce":
                misura.setText("kWh");
                break;
            case "Gas":
                misura.setText("m^3");
                break;
            case "Internet":
                textInputLayout.setVisibility(View.GONE);
                textConsumo.setVisibility(View.GONE);
                misura.setVisibility(View.GONE);
                break;
        }
        btnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String dataScadenza = textDataScadenza.getText().toString();
                    final String periodo = textPeriodo.getText().toString();
                    final String fine = textFine.getText().toString();

                    String tmp = textCosto.getText().toString();
                    final double costo = Double.parseDouble(tmp);
                    if(textConsumo.getVisibility() == View.VISIBLE) {
                        tmp = textConsumo.getText().toString();
                        consumo = Double.parseDouble(tmp);
                    }
                    max= max+1;

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    Date firstDate = sdf.parse(periodo);
                    Date secondDate = sdf.parse(fine);

                    if(firstDate.after(secondDate)){
                        Toast.makeText(getActivity(), R.string.periodoriferimento, Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                    writeBollettaToDb(dataScadenza, periodo, fine, costo, consumo, tipo, currentUser.getUid(), max);
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(),R.string.inforequired, Toast.LENGTH_SHORT).show();
                }

            }
        });

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
                break;
            case 3:
                textFine.setText(sdf.format(myCalendar.getTime()));
                break;

        }

    }

    private void writeBollettaToDb(String dataScadenza, String periodo, String fine, double costo, double consumo, String tipo, String uid, long max) {
        Map<String, Object> bolletta = new HashMap<>();
        bolletta.put("DataScadenza", dataScadenza );
        bolletta.put("Da", periodo);
        bolletta.put("A", fine);
        bolletta.put("Importo", costo);
        bolletta.put("Tipo", tipo);

        if(tipo != "Internet"){
            bolletta.put("Consumo", consumo);
            if (tipo == "Luce"){
                bolletta.put("Misura", "kWh");
            } else {
                bolletta.put("Misura", "m^3");
            }
        }
        bolletta.put("Codice", max);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("utenti").document(uid).collection("bollette").document(tipo + " " + max).set(bolletta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        fragmentFeed = new FragmentFeed();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentFeed).commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }


    }






