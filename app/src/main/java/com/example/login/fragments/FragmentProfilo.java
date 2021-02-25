package com.example.login.fragments;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Html;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.login.MainActivity;
import com.example.login.R;
import com.example.login.entities.BollettaLGI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class FragmentProfilo extends Fragment {

    private FirebaseAuth mAuth;

    private ImageView proPic;
    private TextView textNome;
    private CardView statisticheLuce;
    private CardView statisticheGas;
    private CardView statisticheInternet;
    private TextView textStatisticheLuce;
    private TextView textStatisticheGas;
    private TextView textStatisticheInternet;


    private Button btnLogout, btnModificaPassword, btnSalvaPassword;
    private TextInputEditText textPassword;
    private TextInputLayout passwordLayout;
    private TextInputEditText textPasswordConfirm;
    private TextInputLayout passwordConfirmLayout;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private FirebaseUser currentUser;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int PICK_IMAGE = 200;
    public Uri downloadUri = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilo, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.titoloprofilo));
        proPic = view.findViewById(R.id.propic);
        proPic.setClipToOutline(true);
        textNome = view.findViewById(R.id.text_nome);

        statisticheLuce = view.findViewById(R.id.statistiche_luce);
        statisticheGas = view.findViewById(R.id.statistiche_gas);
        statisticheInternet = view.findViewById(R.id.statistiche_internet);
        textStatisticheLuce = view.findViewById(R.id.text_statistiche_luce);
        textStatisticheGas = view.findViewById(R.id.text_statistiche_gas);
        textStatisticheInternet = view.findViewById(R.id.text_statistiche_internet);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        btnLogout = view.findViewById(R.id.btn_logout);
        btnModificaPassword = view.findViewById(R.id.btn_modifica_password);

        passwordLayout = view.findViewById(R.id.password_layout);
        textPassword = view.findViewById(R.id.text_password);
        passwordConfirmLayout = view.findViewById(R.id.passwordconfirm_layout);
        textPasswordConfirm = view.findViewById(R.id.text_passwordconfirm);
        btnSalvaPassword = view.findViewById(R.id.btn_salva_password);

        textNome.setText( currentUser.getDisplayName());
        if(currentUser.getPhotoUrl() != null){
            Glide.with(this)
                    .load(currentUser.getPhotoUrl())
                    .centerCrop()
                    .into(proPic);
        }
        else {
            Glide.with(this)
                    .load(getActivity().getDrawable(R.drawable.placeholder_icon))
                    .centerCrop()
                    .into(proPic);
        }

        proPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissions.add(Manifest.permission.CAMERA);
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                permissionsToRequest = findUnaskedPermission(permissions);
                if (permissionsToRequest.size() > 0){
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                } else {
                    startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE );
                }
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                SharedPreferences preferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstrun", true);
                editor.apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        statistiche();


        btnModificaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnModificaPassword.setVisibility(View.GONE);
                passwordLayout.setVisibility(View.VISIBLE);
                textPassword.setVisibility(View.VISIBLE);
                passwordConfirmLayout.setVisibility(View.VISIBLE);
                textPasswordConfirm.setVisibility(View.VISIBLE);
                btnSalvaPassword.setVisibility(View.VISIBLE);
                btnSalvaPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                                String newPassword = textPassword.getText().toString();
                                String newPasswordConfirm = textPasswordConfirm.getText().toString();
                                if(!(newPassword.equals(newPasswordConfirm)) ){
                                    Toast.makeText(getActivity(), getString(R.string.passwordnoncorrispondono), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                currentUser.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(),getString( R.string.passwordmodificata), Toast.LENGTH_SHORT).show();
                                                    btnModificaPassword.setVisibility(View.VISIBLE);
                                                    passwordLayout.setVisibility(View.GONE);
                                                    textPassword.setVisibility(View.GONE);
                                                    passwordConfirmLayout.setVisibility(View.GONE);
                                                    textPasswordConfirm.setVisibility(View.GONE);
                                                    btnSalvaPassword.setVisibility(View.GONE);
                                                } else {
                                                    Toast.makeText(getActivity(), getString(R.string.loginpermodifica), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                        } catch(Exception e){
                            Toast.makeText(getActivity(), getString(R.string.inserisciinforichieste), Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
         if (requestCode == PICK_IMAGE) {
            Bitmap bitmap = null;
            if (resultCode == RESULT_OK) {
                if(getPickImageResultUri(intent) != null) {
                    final FirebaseUser user = mAuth.getCurrentUser();
                    Uri picUri = getPickImageResultUri(intent);
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();

                    final StorageReference riversRef = storageRef.child("images/"+picUri.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(picUri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });


                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            return riversRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadUri = task.getResult();
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(downloadUri)
                                        .build();
                                user.updateProfile(profileChangeRequest);
                            } else {
                            }
                        }
                    });


                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    bitmap = (Bitmap) intent.getExtras().get("data");
                }
            }

            Glide.with(this)
                    .load(bitmap)
                    .centerCrop()
                    .into(proPic);
        }
    }

    private Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera =  action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }

        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for(ResolveInfo res:listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() -1);
        for(Intent intent:allIntents) {
            if(intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")){
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, getString(R.string.selsorgente));

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    public Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalCacheDir();
        if(getImage != null){
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "propic.png"));
        }
        return outputFileUri;
    }

    private ArrayList findUnaskedPermission(ArrayList<String> wanted){
        ArrayList<String> result = new ArrayList<>();

        for(String perm: wanted) {
            if(!(getActivity().checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED)) {
                result.add(perm);
            }
        }

        return result;
    }

    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResult) {
        if(requestCode == ALL_PERMISSIONS_RESULT)
            for(String perm: permissionsToRequest) {
                if(getActivity().checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED);{
                    permissionsRejected.add(perm);
                }
            }
        if (permissionsRejected.size() > 0) {
            if(shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                Toast.makeText(getContext(), getString(R.string.approvapermessi), Toast.LENGTH_SHORT).show();
            }
        } else {
            startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE);
        }
    }


    public void statistiche(){

        int cntLuce = 0;
        int cntGas = 0;
        int cntInternet = 0;
        double sommaLuce = 0.0;
        double sommaGas = 0.0;
        double sommaInternet = 0.0;
        double ultimaLuce = 0.0;
        double ultimaGas = 0.0;
        double ultimaInternet = 0.0;

        long maxLuce = 0;
        long maxGas = 0;
        long maxInternet = 0;
        double percentualeLuce = 0;
        double percentualeGas = 0;
        double percentualeInternet = 0;

        ArrayList<BollettaLGI> list= MainActivity.bollette;
        for (BollettaLGI data : list) {
            if(data.getTipo().equals(getString(R.string.luce))) {
                if (data.getId() > maxLuce) {
                    maxLuce = data.getId();
                }
                sommaLuce += data.getCosto();
                cntLuce++;

            }
            if(data.getTipo().equals(getString(R.string.gas))) {
                if (data.getId() > maxGas) {
                    maxGas = data.getId();
                }
                sommaGas += data.getCosto();
                cntGas++;

            }
            if(data.getTipo().equals(getString(R.string.internet))) {
                if (data.getId() > maxInternet) {
                    maxInternet = data.getId();
                }
                sommaInternet += data.getCosto();
                cntInternet++;

            }

        }
        for (BollettaLGI data : list) {
            if((data.getTipo().equals(getString(R.string.luce))) && (data.getId() == maxLuce)) {
                ultimaLuce = data.getCosto();
                sommaLuce -= ultimaLuce;
                cntLuce = cntLuce -1;

            }
            if((data.getTipo().equals(getString(R.string.gas))) && (data.getId() == maxGas)) {
                ultimaGas = data.getCosto();
                sommaGas -= ultimaGas;
                cntGas = cntGas -1;

            }
            if((data.getTipo().equals(getString(R.string.internet))) && (data.getId() == maxInternet)) {
                ultimaInternet = data.getCosto();
                sommaInternet -= ultimaInternet;
                cntInternet = cntInternet -1;

            }

        }
        if (cntLuce <= 0) {
            statisticheLuce.setVisibility(View.GONE);
            textStatisticheLuce.setVisibility(View.GONE);
        } else if (ultimaLuce < (sommaLuce/cntLuce)){
            percentualeLuce = (((sommaLuce/cntLuce) - ultimaLuce)/(sommaLuce/cntLuce))*100;
            String percentualeTroncata = String.format ("%.1f", percentualeLuce);
            textStatisticheLuce.setText(Html.fromHtml("L'ultima bolletta " + "<font color='#f1c40f'><u>" + getString(R.string.luce) + "</u></font>   "+" inserita è stata il " + percentualeTroncata + "%"+ "<font color='#2ecc71'>" + " più economica" + "</font>   "+" delle precedenti"));
        } else if (ultimaLuce > (sommaLuce/cntLuce)){
            percentualeLuce = ((ultimaLuce -(sommaLuce/cntLuce))/(sommaLuce/cntLuce))*100;
            String percentualeTroncata = String.format ("%.1f", percentualeLuce);
            textStatisticheLuce.setText(Html.fromHtml("L'ultima bolletta " + "<font color='#f1c40f'><u>" + getString(R.string.luce) + "</u></font>   "+" inserita è stata il " + percentualeTroncata + "%"+ "<font color='#e74c3c'>" + " più cara" + "</font>   "+" delle precedenti"));
        }

        if (cntGas <= 0) {
            statisticheGas.setVisibility(View.GONE);
            textStatisticheGas.setVisibility(View.GONE);
        } else if (ultimaGas < (sommaGas/cntGas)){
            percentualeGas = (((sommaGas/cntGas) - ultimaGas)/(sommaGas/cntGas))*100;
            String percentualeTroncata = String.format ("%.1f", percentualeGas);
            textStatisticheGas.setText(Html.fromHtml("L'ultima bolletta " + "<font color='#ff5722'><u>" + getString(R.string.gas) + "</u></font>   "+" inserita è stata il " + percentualeTroncata + "%"+ "<font color='#2ecc71'>" + " più economica" + "</font>   "+" delle precedenti"));
        } else if (ultimaGas > (sommaLuce/cntGas)){
            percentualeGas = ((ultimaGas -(sommaGas/cntGas))/(sommaGas/cntGas))*100;
            String percentualeTroncata = String.format ("%.1f", percentualeGas);
            textStatisticheGas.setText(Html.fromHtml("L'ultima bolletta " + "<font color='#ff5722'><u>" + getString(R.string.gas) + "</u></font>   "+" inserita è stata il " + percentualeTroncata + "%"+ "<font color='#e74c3c'>" + " più cara" + "</font>   "+" delle precedenti"));
        }

        if (cntInternet == 0) {
            statisticheInternet.setVisibility(View.GONE);
            textStatisticheInternet.setVisibility(View.GONE);
        } else if (ultimaInternet <= (sommaInternet/cntInternet)){
            percentualeInternet = (((sommaInternet/cntInternet) - ultimaInternet)/(sommaInternet/cntInternet))*100;
            String percentualeTroncata = String.format ("%.1f", percentualeInternet);
            textStatisticheInternet.setText(Html.fromHtml("L'ultima bolletta "+ "<font color='#80deea'><u>"+ getString(R.string.internet) + "</u></font>" + " inserita è stata il " + percentualeTroncata + "%"+ "<font color='#2ecc71'>" + " più economica" + "</font>   "+" delle precedenti"));
        } else if (ultimaInternet > (sommaInternet/cntInternet)){
            percentualeInternet = ((ultimaInternet -(sommaInternet/cntInternet))/(sommaInternet/cntInternet))*100;
            String percentualeTroncata = String.format ("%.1f", percentualeInternet);
            textStatisticheInternet.setText(Html.fromHtml("L'ultima bolletta "+ "<font color='#80deea'>"+ getString(R.string.internet) + "</font>   "+" inserita è stata il " + percentualeTroncata + "%"+ "<font color='#e74c3c'>" + " più cara" + "</font>   "+" delle precedenti"));
        }

    }
}
