package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.bumptech.glide.signature.ObjectKey;
import com.example.login.entities.Bolletta;
import com.example.login.entities.BollettaLuce;
import com.example.login.fragments.FragmentFeed;
import com.example.login.fragments.FragmentProfilo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_REQUEST = 101;
    private BottomNavigationView bottomNav;
    private FragmentProfilo fragmentProfilo;
    private FragmentFeed fragmentFeed;

    ArrayList<Object> bollette = new ArrayList<Object>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        if (preferences.getBoolean("firstrun", true)) {
            Intent intent = new Intent(MainActivity.this, RegistrazioneActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
        } else {
            getSupportActionBar().setTitle(getString(R.string.welcome));

            fragmentFeed = new FragmentFeed();
            fragmentProfilo = new FragmentProfilo();


            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentFeed).commit();

            bottomNav = findViewById(R.id.bottom_nav);
            bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.menu_feed:
                            selectedFragment = fragmentFeed;
                            break;
                        case R.id.menu_profilo:
                            selectedFragment = fragmentProfilo;
                            break;
                    }
                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;
                }
            });
        }

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                String nome = intent.getExtras().getString("nome");
                String cognome = intent.getExtras().getString("cognome");

                getSupportActionBar().setTitle(nome + " " + cognome);

                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstrun", false);
                editor.apply();
            }
            if (resultCode == RESULT_FIRST_USER) {
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstrun", false);
                editor.apply();
            }
        }

    }


}