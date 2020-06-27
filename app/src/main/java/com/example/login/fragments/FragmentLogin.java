package com.example.login.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.app.Activity.RESULT_FIRST_USER;

public class FragmentLogin extends Fragment {
    private FirebaseAuth mAuth;
    private  TextInputEditText textEmail;
    private TextInputEditText textPassword;
    private Button btnLogin, btnRegistra;
    private String email = "";
    private String password = "";
    FragmentRegistrazione fragmentRegistrazione;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        textEmail = view.findViewById(R.id.email);
        textPassword = view.findViewById(R.id.password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnRegistra = view.findViewById(R.id.btn_registra);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                email = textEmail.getText().toString();
                password = textPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent();
                                    getActivity().setResult(RESULT_FIRST_USER,intent);
                                    getActivity().finish();

                                } else {
                                    Toast.makeText(getActivity(), R.string.autenticazionefallita,
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }  catch (Exception e) {
                Toast.makeText(getActivity(), R.string.inforequired, Toast.LENGTH_SHORT).show();
            }
        }
        });

        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = textEmail.getText().toString();
                password = textPassword.getText().toString();
                fragmentRegistrazione = new FragmentRegistrazione();
                Bundle args = new Bundle();
                args.putString("email", email);
                args.putString("password", password);
                fragmentRegistrazione.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragmentRegistrazione).addToBackStack(null).commit();
            }
        });
        return view;
    }







}
