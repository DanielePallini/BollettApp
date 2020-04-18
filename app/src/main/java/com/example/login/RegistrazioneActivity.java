package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.example.login.fragments.FragmentButtons;
import com.example.login.fragments.FragmentRegistrazione;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseUser;

public class RegistrazioneActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FragmentButtons fragmentButtons;

    private final static int SIGN_IN_REQUEST = 236;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);
        fragmentButtons = new FragmentButtons();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragmentButtons).commit();

    }

}


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == SIGN_IN_REQUEST) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
    */
