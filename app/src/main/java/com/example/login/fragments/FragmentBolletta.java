package com.example.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.google.android.material.textfield.TextInputEditText;

public class FragmentBolletta extends Fragment {
    private TextInputEditText textDataScadenza, textPeriodo, textCosto, textConsumo;
    private TextView euro, kwh;
    private Button btnSalva;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bolletta, container, false);
        textDataScadenza = view.findViewById(R.id.text_data_scadenza);
        textPeriodo = view.findViewById(R.id.text_periodo_riferimento);
        textCosto = view.findViewById(R.id.text_costo);
        euro = view.findViewById(R.id.euro);
        textConsumo = view.findViewById(R.id.text_consumo);
        kwh = view.findViewById(R.id.misura);
        btnSalva = view.findViewById(R.id.btn_salva);
        return view;
    }
}
