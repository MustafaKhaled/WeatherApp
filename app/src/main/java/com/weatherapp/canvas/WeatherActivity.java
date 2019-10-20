package com.weatherapp.canvas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.security.Permission;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class WeatherActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    final static int CAMERA_PICTURE_REQUEST = 1;
    @BindView(R.id.fab)
    FloatingActionButton cameraBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.fab)
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.fab:
                if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)){
                    openCamera();
                }
                else{
                    requestCamera();
                }
                break;
        }
    }

    private void requestCamera() {
        EasyPermissions.requestPermissions(this,
                "Camera Permission is required",
                CAMERA_PICTURE_REQUEST,
                Manifest.permission.CAMERA
                );
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_PICTURE_REQUEST);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
