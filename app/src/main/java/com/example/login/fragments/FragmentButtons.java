package com.example.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.google.android.gms.common.SignInButton;

public class FragmentButtons extends Fragment {

    private Button btnlogin;
    private Button btnregistra;
    private FragmentRegistrazione fragmentRegistrazione;
    private SignInButton signInButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);
        btnlogin = view.findViewById(R.id.login_button);
        btnregistra = view.findViewById(R.id.registra_button);
        signInButton = view.findViewById(R.id.sign_in_button);
        btnregistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentRegistrazione = new FragmentRegistrazione();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragmentRegistrazione).commit();
            }
        });
        return view;
    }

}
