package com.example.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;

public class FragmentSelezioneBolletta extends Fragment implements View.OnClickListener {
    private ImageButton btnAddLuce;
    private ImageButton btnAddGas;
    private ImageButton btnAddInternet;
    FragmentBolletta fragmentBolletta;
    int maxLuce = 0;
    int maxGas = 0;
    int maxInternet = 0;
    String tipo = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selezione_bolletta, container, false);
        btnAddLuce = view.findViewById(R.id.button_add_bolletta_luce);
        btnAddGas = view.findViewById(R.id.button_add_bolletta_gas);
        btnAddInternet = view.findViewById(R.id.button_add_bolletta_internet);
        btnAddLuce.setOnClickListener(this);
        btnAddGas.setOnClickListener(this);
        btnAddInternet.setOnClickListener(this);
        Bundle args = getArguments();
        maxLuce = args.getInt("maxLuce", 0);
        maxGas = args.getInt("maxGas", maxGas);
        maxInternet = args.getInt("maxInternet", maxInternet);
        return view;
    }

    @Override
    public void onClick(View v) {
        fragmentBolletta = new FragmentBolletta();
        Bundle args = new Bundle();
        switch (v.getId()){
            case R.id.button_add_bolletta_luce:
                tipo = "Luce";
                args.putInt("max", maxLuce);
                args.putString("tipo", tipo);
                fragmentBolletta.setArguments(args);
                break;
            case R.id.button_add_bolletta_gas:
                tipo = "Gas";
                args.putInt("max", maxGas);
                args.putString("tipo", tipo);
                fragmentBolletta.setArguments(args);
                break;
            case R.id.button_add_bolletta_internet:
                tipo = "Internet";
                args.putInt("max", maxInternet);
                args.putString("tipo", tipo);
                fragmentBolletta.setArguments(args);
                break;

        }
        if (args!= null){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentBolletta).commit();
        }
    }
}
