package com.example.login.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.MainActivity;
import com.example.login.R;
import com.example.login.entities.BollettaLGI;
import com.example.login.uiutilities.FeedAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentFeed extends Fragment implements FeedAdapter.OnDeleteListener {

    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private ArrayList<BollettaLGI> bollette;
    private ImageButton btnAdd;
    private FragmentSelezioneBolletta fragmentSelezioneBolletta;
    private FirebaseAuth mAuth;
    long maxLuce = 0;
    long maxGas = 0;
    long maxInternet = 0;
    String tipo = "";
    FirebaseFirestore db;
    FirebaseUser currentUser;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bollette = new ArrayList<>();
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        recyclerView = view.findViewById(R.id.rv_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        feedAdapter = new FeedAdapter(bollette, this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(feedAdapter);

        btnAdd = view.findViewById(R.id.button_add);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        scaricaDati(db, currentUser.getUid());



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentSelezioneBolletta = new FragmentSelezioneBolletta();
                Bundle args = new Bundle();
                args.putLong("maxLuce", maxLuce);
                args.putLong("maxGas", maxGas);
                args.putLong("maxInternet", maxInternet);
                fragmentSelezioneBolletta.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentSelezioneBolletta).addToBackStack(null).commit();
            }
        });

        return view;
    }
    public void scaricaDati(FirebaseFirestore db, String uid) {
        db.collection("utenti").document(uid).collection("bollette")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {




                        if (task.isSuccessful()) {
                            String filterLuce = "Luce";
                            String filterGas = "Gas";
                            String filterInternet = "Internet";

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //String st = document.getId();
                                /*
                                String obj = document.getData().toString();

                                //document.getString("A");
                                String[] str = obj.split(",");
                                String[] Dati = new String[6];

                                for(int i = 0; i < 6; i++){
                                    String[] str1 = str[i].split("=");
                                    Dati[i] = str1[1];
                                    //Log.d(TAG, Dati[i]);
                                }
                                Dati[5] = Dati[5].substring(0, Dati[5].length() - 1);//metodo agricolo ma efficace

                                 */
                                if (document.getId().matches(filterLuce + "(.*)")) {
                                    tipo = "Luce";
                                }
                                if (document.getId().matches(filterGas + "(.*)")) {
                                    tipo = "Gas";
                                }
                                if (document.getId().matches(filterInternet + "(.*)")) {
                                    tipo = "Internet";
                                }

                                try {
                                    //Object tmp = document.get("Codice");

                                    BollettaLGI bollettaLGI = new BollettaLGI(document.getLong("Codice") , document.getDouble("Importo"), document.getString("Data Scadenza"), document.getString("Da"), document.getString("A"), document.getDouble("Consumo"), tipo);

                                    bollette.add(bollettaLGI);
                                    if (tipo == "Luce") {
                                        if (bollettaLGI.getId() > maxLuce) {
                                            maxLuce = bollettaLGI.getId();
                                        }
                                    }
                                    if (tipo == "Gas") {
                                        if (bollettaLGI.getId() > maxGas) {
                                            maxGas = bollettaLGI.getId();
                                        }
                                    }
                                    if (tipo == "Internet") {
                                        if (bollettaLGI.getId() > maxInternet) {
                                            maxInternet = bollettaLGI.getId();
                                        }
                                    }
                                    feedAdapter.notifyDataSetChanged();
                                    MainActivity.bollette = bollette;
                                    //Log.d(TAG, max);

                                } catch (NullPointerException e) {
                                    Log.d(TAG, "Nessun campo trovato");
                                }

                                //BollettaLGI bollettaLGI = new BollettaLGI(Integer.parseInt(Dati[3]), Double.parseDouble(Dati[5]), Dati[1], Dati[4], Dati[0], Double.parseDouble(Dati[2]),tipo);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });

    }

    @Override
    public void onDeleteClick(final String tipo, final long codice) {
        Log.d(TAG, "onDeleteClick: " + tipo + " " + codice);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        builder1.setMessage("Sei davvero sicuro di eliminare questa bolletta?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        db.collection("utenti").document(currentUser.getUid()).collection("bollette").document(tipo + " " + codice)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        FragmentFeed fragmentFeed = new FragmentFeed();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentFeed).commit();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                });

        builder1.setNegativeButton(
                "Annulla",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


}
