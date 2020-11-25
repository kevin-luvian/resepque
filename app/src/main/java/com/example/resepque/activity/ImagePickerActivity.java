package com.example.resepque.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.resepque.R;

import java.util.ArrayList;
import java.util.List;

public class ImagePickerActivity extends AppCompatActivity {
    private static final int MULTIPLE_PERMISSION_CODE = 7;
    private static final String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public interface PickerOptionListener {
        void onTakeCameraSelected();

        void onChooseGallerySelected();
    }

    public static void showImagePickerOptions(Activity context, PickerOptionListener listener) {
        boolean checkPermission = checkPermissions(context, permissions);
        System.out.println("Checking permissions : " + checkPermission);
        if (checkPermission) {
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.label_choose_image));

            // add a list
            String[] labels = {context.getString(R.string.label_take_camera), context.getString(R.string.label_choose_gallery)};
            builder.setItems(labels, (dialog, which) -> {
                switch (which) {
                    case 0:
                        listener.onTakeCameraSelected();
                        break;
                    case 1:
                        if (checkPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}))
                            listener.onChooseGallerySelected();
                        break;
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public static boolean checkPermissions(Activity activity, String[] permissions) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String permission : permissions) {
            result = ContextCompat.checkSelfPermission(activity, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Permission result: " + result + " granted: " + PackageManager.PERMISSION_GRANTED);
                listPermissionsNeeded.add(permission);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    listPermissionsNeeded.toArray(new String[0]), MULTIPLE_PERMISSION_CODE);
            return false;
        }
        return true;
    }
}