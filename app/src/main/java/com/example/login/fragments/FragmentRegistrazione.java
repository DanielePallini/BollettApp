package com.example.login.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FragmentRegistrazione extends Fragment {

    private TextInputEditText textNome, textCognome, textEmail, textPassword;
    private Button btnRegistra;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_registrazione, container, false);

        mAuth = FirebaseAuth.getInstance();

        textNome = view.findViewById(R.id.text_nome);
        textCognome = view.findViewById(R.id.text_cognome);
        textEmail = view.findViewById(R.id.text_email);
        textPassword = view.findViewById(R.id.text_password);
        btnRegistra = view.findViewById(R.id.btn_registra);
        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String nome = textNome.getText().toString();
                    final String cognome = textCognome.getText().toString();
                    String email = textEmail.getText().toString();
                    String password = textPassword.getText().toString();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final FirebaseUser user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nome + " " + cognome)
                                        .build();
                                user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        writeUserToDb(nome, cognome, user.getUid());
                                        Intent intent = new Intent();
                                        intent.putExtra("nome", textNome.getText().toString());
                                        intent.putExtra("cognome", textCognome.getText().toString());
                                        getActivity().setResult(RESULT_OK,intent);
                                        getActivity().finish();
                                    }
                                });
                            }
                            else {
                                Toast.makeText(getActivity(), getString(R.string.errorsignup), Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
        //getActivity().getSupportActionBar().setTitle("REGISTRATI");
    }

    private void writeUserToDb(String nome, String cognome, String uid) {
        Map<String, Object> user = new HashMap<>();
        user.put("nome", nome );
        user.put("cognome", cognome);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("utenti").document(uid).set(user);
    }
}
