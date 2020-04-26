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
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_REQUEST = 101;
    private BottomNavigationView bottomNav;
    private FragmentProfilo fragmentProfilo;
    private FragmentFeed fragmentFeed;
    private FirebaseAuth mAuth;
    int max = 0;
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
            mAuth = FirebaseAuth.getInstance();
            final FirebaseUser currentUser = mAuth.getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            scaricaDati(db, currentUser.getUid());

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

    public void scaricaDati(FirebaseFirestore db, String uid) {
        String finePeriodo = "";
        String periodo = "";
        int codice = 0;
        double consumo = 0;
        double costo = 0;
        String dataScadenza = "";
        db.collection("utenti").document(uid).collection("bollette")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {




                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //String st = document.getId();
                                String obj = document.getData().toString();
                                String[] str = obj.split(",");
                                String[] Dati = new String[6];

                                for(int i = 0; i < 6; i++){
                                /*{
                                    switch(i){
                                        case 0:

                                    }

                                 */
                                    String[] str1 = str[i].split("=");
                                    Dati[i] = str1[1];
                                    //Log.d(TAG, Dati[i]);

                                }
                                Dati[5] = Dati[5].substring(0, Dati[5].length() - 1); //metodo agricolo ma efficace
                                BollettaLuce bollettaLuce = new BollettaLuce(Integer.parseInt(Dati[3]), Double.parseDouble(Dati[5]), Dati[1], Dati[4], Dati[0], Double.parseDouble(Dati[2]));
                                bollette.add(bollettaLuce);
                                //Log.d(TAG, bollette.toString());
                                if (bollettaLuce.getId() > max) {
                                    max = bollettaLuce.getId();
                                }

                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //Log.d(TAG, max);
                            }
                            //Log.d(TAG, max);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });

    }
}