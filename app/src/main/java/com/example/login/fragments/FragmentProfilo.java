package com.example.login.fragments;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FragmentProfilo extends Fragment {

    private FirebaseAuth mAuth;

    private ImageView proPic;
    private TextView textNome;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int PICK_IMAGE = 200;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilo, container, false);
        proPic = view.findViewById(R.id.propic);
        proPic.setClipToOutline(true);
        textNome = view.findViewById(R.id.text_nome);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        textNome.setText(currentUser.getDisplayName());
        if(currentUser.getPhotoUrl() != null){
            Glide.with(this)
                    .load(currentUser.getPhotoUrl())
                    .centerCrop()
                    .into(proPic);
        }
        else {
            Glide.with(this)
                    .load(getActivity().getDrawable(R.drawable.ic_person_black_24dp))
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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
         if (requestCode == PICK_IMAGE) {
            Bitmap bitmap = null;
            if (resultCode == RESULT_OK) {
                if(getPickImageResultUri(intent) != null) {
                    Uri picUri = getPickImageResultUri(intent);
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

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res:listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if(outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

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
                Toast.makeText(getContext(), "Approva tutto", Toast.LENGTH_SHORT).show();
            }
        } else {
            startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE);
        }
    }
}
