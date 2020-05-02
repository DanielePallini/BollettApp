package com.example.login.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.example.login.entities.BollettaLuce;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentGrafici extends Fragment {

    private FirebaseAuth mAuth;
    private ArrayList<BollettaLuce> bollette;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bollette = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grafici, container, false);

        //BarChart chart = view.findViewById(R.id.barchart);

        LineChart chart = (LineChart) view.findViewById(R.id.chart);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        scaricaDati(db, currentUser.getUid());




        return view;
    }

    public void scaricaDati(FirebaseFirestore db, String uid) {
        db.collection("utenti").document(uid).collection("bollette")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {




                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //String st = document.getId();
                                String obj = document.getData().toString();
                                String[] str = obj.split(",");
                                String[] Dati = new String[6];

                                for(int i = 0; i < 6; i++){
                                /*{
                                    switch(i){
                                        case 0:

                                    }

                                 */
                                    String[] str1 = str[i].split("=");
                                    Dati[i] = str1[1];
                                    //Log.d(TAG, Dati[i]);

                                }
                                Dati[5] = Dati[5].substring(0, Dati[5].length() - 1); //metodo agricolo ma efficace
                                BollettaLuce bollettaLuce = new BollettaLuce(Integer.parseInt(Dati[3]), Double.parseDouble(Dati[5]), Dati[1], Dati[4], Dati[0], Double.parseDouble(Dati[2]));
                                bollette.add(bollettaLuce);


                            }



                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });

    }
}
