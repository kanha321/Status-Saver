package com.kanha.statussaver;

import static android.os.Build.VERSION.SDK_INT;

import static com.kanha.statussaver.Util.Resources.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;

import com.kanha.statussaver.Util.MyToast;
import com.kanha.statussaver.Util.Resources;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";

    Dialog dialog;

    public static boolean permissionGranted;
    boolean showDialog = true;

    boolean isSaveFolderCreated;

    static final int PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        isSaveFolderCreated = STATUS_SAVER_DIR.mkdir();
        init();

    }

    void init(){

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.permission_request);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.create();

        dialog.findViewById(R.id.ok).setOnClickListener(v -> requestPermission());

        dialog.findViewById(R.id.exit).setOnClickListener(v -> finishAffinity());

        takePermission();

        if (isPermissionGranted()) {
            permissionGranted = true;
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void takePermission() {
        if (!isPermissionGranted() && showDialog) {
            dialog.show();
        } else {
            showDialog = false;
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public boolean isPermissionGranted() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionGranted = readExternalStoragePermission == PackageManager.PERMISSION_GRANTED;
            return readExternalStoragePermission == PackageManager.PERMISSION_GRANTED;
        }
    }

    public void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                // perform action when allow permission success
                MyToast.showToast(this, "Permission Granted");
                Log.d(TAG, "onRequestPermissionsResult: a10- Permission Granted");
                permissionGranted = true;
                dialog.dismiss();
            } else {
                MyToast.showToast(this, "Permission Denied");
                Log.d(TAG, "onRequestPermissionsResult: a10- Permission Denied");
                permissionGranted = false;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2296) {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        // perform action when allow permission success
                        MyToast.showToast(this, "Permission granted");
                        Log.d(TAG, "onActivityResult: Permission granted a11+");
                        permissionGranted = true;
                        dialog.dismiss();
                    } else {
                        MyToast.showToast(this, "Permission denied");
                        Log.d(TAG, "onActivityResult: permission denied a11+");
                        permissionGranted = false;
                        takePermission();
                    }
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isPermissionGranted()) {
            permissionGranted = true;
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (isSaveFolderCreated) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }
}