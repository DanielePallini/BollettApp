package com.example.login.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentBolletta extends Fragment {
    private TextInputEditText textDataScadenza, textPeriodo, textFine, textCosto, textConsumo;
    private TextView euro, kwh;
    private Button btnSalva;
    private int num = 0;
    final Calendar myCalendar = Calendar.getInstance();
    private FirebaseAuth mAuth;
    private FragmentFeed fragmentFeed;
    private int max = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bolletta, container, false);
        textDataScadenza = view.findViewById(R.id.text_data_scadenza);
        textPeriodo = view.findViewById(R.id.text_periodo_riferimento);
        textFine = view.findViewById(R.id.text_fine_riferimento);
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
        kwh = view.findViewById(R.id.misura);
        btnSalva = view.findViewById(R.id.btn_salva);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        btnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String dataScadenza = textDataScadenza.getText().toString();
                    final String periodo = textPeriodo.getText().toString();
                    final String fine = textFine.getText().toString();
                    String tmp = textCosto.getText().toString();
                    final double costo = Double.parseDouble(tmp);
                    tmp = textConsumo.getText().toString();
                    final double consumo = Double.parseDouble(tmp);


                    Bundle args = getArguments();
                    max = args.getInt("max", 0);
                    max= max+1;
                    String tipo = "Luce ";
                    //tipo += codice;
                    writeBollettaToDb(dataScadenza, periodo, fine, costo, consumo, tipo, currentUser.getUid(), max);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
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

    private void writeBollettaToDb(String dataScadenza, String periodo, String fine, double costo, double consumo, String tipo, String uid, int max) {
        Map<String, Object> bolletta = new HashMap<>();
        bolletta.put("Data Scadenza", dataScadenza );
        bolletta.put("Da", periodo);
        bolletta.put("A", fine);
        bolletta.put("Importo", costo);
        bolletta.put("Consumo", consumo);
        bolletta.put("Codice", max);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("utenti").document(uid).collection("bollette").document(tipo + "" + max).set(bolletta)
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

     /*DocumentReference docRef = db.collection("utenti").document(uid).collection("bollette").document("Luce 2");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //String obj = document.getData().toString();
                        //String[] str = obj.split(",");
                        //String[] str1 = str[3].split("=");
                        //int codice = Integer.parseInt(str1[1]);
                        Log.d(TAG, "DocumentSnapshot id: " + document.getId());
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        db.collection("utenti").document(uid).collection("bollette")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String max = "";
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String st = document.getId();

                                if (st.compareTo(max) > 0){
                                    max = st;

                                }
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //Log.d(TAG, max);
                            }

                            Log.d(TAG, max);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        */

//db.collection("utenti").document(uid).collection("bollette").document("Luce 1").get();




