package com.example.login.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.MainActivity;
import com.example.login.R;
import com.example.login.entities.Bolletta;
import com.example.login.entities.BollettaLuce;
import com.example.login.uiutilities.FeedAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentFeed extends Fragment {

    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private ArrayList<BollettaLuce> bollette;
    private ImageButton btnAdd;
    private FragmentSelezioneBolletta fragmentSelezioneBolletta;
    private FirebaseAuth mAuth;
    int max = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bollette = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_feed, container, false);


        recyclerView = view.findViewById(R.id.rv_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        feedAdapter = new FeedAdapter(bollette);
        recyclerView.setAdapter(feedAdapter);

        btnAdd = view.findViewById(R.id.button_add);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        scaricaDati(db, currentUser.getUid());



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentSelezioneBolletta = new FragmentSelezioneBolletta();
                Bundle args = new Bundle();
                args.putInt("max", max);
                fragmentSelezioneBolletta.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentSelezioneBolletta).commit();
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

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //String st = document.getId();
                                String obj = document.getData().toString();
                                String[] str = obj.split(",");
                                String[] Dati = new String[6];

                                for(int i = 0; i < 6; i++){
                                    String[] str1 = str[i].split("=");
                                    Dati[i] = str1[1];
                                    //Log.d(TAG, Dati[i]);

                                }
                                Dati[5] = Dati[5].substring(0, Dati[5].length() - 1); //metodo agricolo ma efficace
                                BollettaLuce bollettaLuce = new BollettaLuce(Integer.parseInt(Dati[3]), Double.parseDouble(Dati[5]), Dati[1], Dati[4], Dati[0], Double.parseDouble(Dati[2]));
                                bollette.add(bollettaLuce);
                                //Log.d(TAG, bollette.toString());
                                if (bollettaLuce.getId() > max) {
                                    max = bollettaLuce.getId();

                                }
                                //Log.d(TAG, String.valueOf(max));



                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //Log.d(TAG, max);
                            }
                            feedAdapter.notifyDataSetChanged();
                            MainActivity.bollette = bollette;
                            //Log.d(TAG, max);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });

    }
}
