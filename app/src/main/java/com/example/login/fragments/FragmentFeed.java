package com.example.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.entities.Recensione;
import com.example.login.uiutilities.FeedAdapter;

import java.util.ArrayList;

public class FragmentFeed extends Fragment {

    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private ArrayList<Recensione> recensioni;
    private ImageButton btnAdd;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        recensioni = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_feed, container, false);


        recyclerView = view.findViewById(R.id.rv_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        feedAdapter = new FeedAdapter(recensioni);
        recyclerView.setAdapter(feedAdapter);

        btnAdd = view.findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titolo =  randomString(15);
                int voto = 5;
                String testo = randomString(90);
                Recensione recensione = new Recensione(titolo, voto, testo);
                recensioni.add(recensione);

                feedAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
    private String randomString(int count){
        final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        while (count-- != 0){
            int character = (int) (Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
