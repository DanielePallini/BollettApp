package com.example.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.fragments.FragmentLogin;


public class RegistrazioneActivity extends AppCompatActivity {

    private FragmentLogin fragmentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);
        fragmentLogin = new FragmentLogin();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragmentLogin).commit();

    }

    @Override
    public void onBackPressed() {


    }
}

