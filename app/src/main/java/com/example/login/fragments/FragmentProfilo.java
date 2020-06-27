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
import android.util.Log;
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
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.login.MainActivity;
import com.example.login.R;
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
import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentProfilo extends Fragment {

    private FirebaseAuth mAuth;

    private ImageView proPic;
    private TextView textNome;
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.titoloprofilo);
        proPic = view.findViewById(R.id.propic);
        proPic.setClipToOutline(true);
        textNome = view.findViewById(R.id.text_nome);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        btnLogout = view.findViewById(R.id.btn_logout);
        btnModificaPassword = view.findViewById(R.id.btn_modifica_password);

        passwordLayout = view.findViewById(R.id.password_layout);
        textPassword = view.findViewById(R.id.text_password);
        passwordConfirmLayout = view.findViewById(R.id.passwordconfirm_layout);
        textPasswordConfirm = view.findViewById(R.id.text_passwordconfirm);
        btnSalvaPassword = view.findViewById(R.id.btn_salva_password);

        textNome.setText(currentUser.getDisplayName());
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
                                    Toast.makeText(getActivity(), R.string.passwordnoncorrispondono, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                currentUser.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), R.string.passwordmodificata, Toast.LENGTH_SHORT).show();
                                                    btnModificaPassword.setVisibility(View.VISIBLE);
                                                    passwordLayout.setVisibility(View.GONE);
                                                    textPassword.setVisibility(View.GONE);
                                                    passwordConfirmLayout.setVisibility(View.GONE);
                                                    textPasswordConfirm.setVisibility(View.GONE);
                                                    btnSalvaPassword.setVisibility(View.GONE);
                                                } else {
                                                    Toast.makeText(getActivity(), R.string.loginpermodifica, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                        } catch(Exception e){
                            Toast.makeText(getActivity(), R.string.inserisciinforichieste, Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), R.string.approvapermessi, Toast.LENGTH_SHORT).show();
            }
        } else {
            startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE);
        }
    }
}
