package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.MeasureUnit;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.login.entities.BollettaLGI;
import com.example.login.fragments.FragmentFeed;
import com.example.login.fragments.FragmentGrafici;
import com.example.login.fragments.FragmentProfilo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_REQUEST = 101;
    private BottomNavigationView bottomNav;
    private FragmentProfilo fragmentProfilo;
    private FragmentFeed fragmentFeed;
    private FragmentGrafici fragmentGrafici;
    public static ArrayList<BollettaLGI> bollette;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bollette = new ArrayList<>();


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        if (preferences.getBoolean("firstrun", true)) {
            Intent intent = new Intent(MainActivity.this, RegistrazioneActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
        } else {
            mAuth = FirebaseAuth.getInstance();
            final FirebaseUser currentUser = mAuth.getCurrentUser();
            getSupportActionBar().setTitle(getString(R.string.welcome) + currentUser.getDisplayName());
            fragmentFeed = new FragmentFeed();
            fragmentProfilo = new FragmentProfilo();
            fragmentGrafici = new FragmentGrafici();


            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentFeed).addToBackStack(null).commit();

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
                        case R.id.menu_grafici:
                            selectedFragment = fragmentGrafici;
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
            /*
            if (resultCode == RESULT_OK) {

                String nome = intent.getExtras().getString("nome");
                String cognome = intent.getExtras().getString("cognome");

                getSupportActionBar().setTitle(nome + " " + cognome);

                 */

                if (resultCode == RESULT_FIRST_USER || resultCode == RESULT_OK) {
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstrun", false);
                editor.apply();
                Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent2);
            }
                /*
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstrun", false);
                editor.apply();
                Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent2);

                 */
            }
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Both navigation bar back press and title bar back press will trigger this method
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    }
