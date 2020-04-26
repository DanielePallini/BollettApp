package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_REQUEST = 101;
    private BottomNavigationView bottomNav;
    private FragmentProfilo fragmentProfilo;
    private FragmentFeed fragmentFeed;
    private FirebaseAuth mAuth;


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
        db.collection("utenti").document(uid).collection("bollette")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String max = "";
                        if (task.isSuccessful()) {
                            Map<String, Object> bolletta = new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String st = document.getId();
                                String obj = document.getData().toString();
                                BollettaLuce bollettaLuce = new BollettaLuce(0, 0, "", "", 0, "khw", 0);
                                bolletta.put("Data Scadenza", dataScadenza );
                                bolletta.put("Da", periodo);
                                bolletta.put("A", fine);
                                bolletta.put("Importo", costo);
                                bolletta.put("Consumo", consumo);
                                /*String[] str = obj.split(",");
                                int j = 0;
                                String[] str1 = str[0].split("=");
                                for(j = 0, j<6, j++){
                                    str1 = str[j].split("=");

                                    j++;
                                }

                                 */

                                //int codice = Integer.parseInt(str1[1]);

                                if (st.compareTo(max) > 0) {
                                    max = st;

                                }
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //Log.d(TAG, max);
                            }
                            Log.d(TAG, max);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}
