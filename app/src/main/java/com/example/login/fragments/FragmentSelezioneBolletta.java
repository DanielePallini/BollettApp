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
    FragmentBolletta fragmentBolletta;
    int max = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selezione_bolletta, container, false);
        btnAddLuce = view.findViewById(R.id.button_add_bolletta_luce);
        btnAddLuce.setOnClickListener(this);
        Bundle args = getArguments();
        max = args.getInt("max", 0);
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;
        switch (v.getId()){
            case R.id.button_add_bolletta_luce:
                fragmentBolletta = new FragmentBolletta();
                Bundle args = new Bundle();
                args.putInt("max", max);
                fragmentBolletta.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentBolletta).commit();
                break;

        }
    }
}
