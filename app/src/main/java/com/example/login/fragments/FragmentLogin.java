package com.example.login.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.MainActivity;
import com.example.login.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.login.RegistrazioneActivity;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

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
                email = textEmail.getText().toString();
                password = textPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    String nome;
                                    String cognome;
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //nome = user
                                    //cognome = user.
                                    Intent intent = new Intent();
                                    //intent.putExtra("nome", nome);
                                    //intent.putExtra("cognome", cognome);
                                    getActivity().setResult(RESULT_FIRST_USER,intent);
                                    getActivity().finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("SignInFailed", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }



                            }
                        });

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

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragmentRegistrazione).commit();
            }
        });
        return view;
    }







}
