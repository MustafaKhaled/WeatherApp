package com.weatherapp.canvas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.weatherapp.canvas.util.FileHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class WeatherActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "WeatherActivity";
    private Uri photoURI;
    private File copiedFile, resultFile;
    private File photoFile;
    final static int CAMERA_PICTURE_REQUEST = 1;
    final static int CAMERA_RESULT = 2;
    private ArrayList<String> f = new ArrayList<String>();// list of file paths
    private File[] listFile;
    @BindView(R.id.fab)
    FloatingActionButton cameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getAllFiles();

        Log.d(TAG, "onCreate: File size length: " + listFile.length);


    }

    @OnClick(R.id.fab)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                    openCamera();
                } else {
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = FileHelper.createImageFile(this);

            } catch (IOException ex) {
                // Error occurred while creating the Fil
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Bitmap bitmap = FileHelper.createBitmapFromFile(n);
                photoURI = FileProvider.getUriForFile(this,
                        "com.weather.canvas.fileprovider",
                        photoFile);
                Log.d(TAG, "openCamera: Uri size : " + photoFile.length() + "Filename :" + photoFile.getName());
                deleteFile(photoFile.getName());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_RESULT);
                photoFile.deleteOnExit();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == CAMERA_PICTURE_REQUEST) {
            openCamera();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_RESULT &&
                resultCode == RESULT_OK
        ) {
            File file = FileHelper.getFileFromUri(getApplicationContext(), photoURI); //uri is checked fro nullability
            Runnable runnable = () -> {
                Bitmap bitmap = FileHelper.createBitmapFromFile(file.getPath());
                Bitmap result = FileHelper.drawTextToBitmap(getApplicationContext(), bitmap, "This is Text View");
//                bitmap.recycle();
                try {
                    FileHelper.createFileFromBitmap(result, getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            runnable.run();
            if (photoFile.exists()) {
                photoFile.delete();
            }
        }

    }

    private void getAllFiles() {
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());

        if (file.isDirectory()) {
            listFile = file.listFiles();


            for (int i = 0; i < listFile.length; i++) {

                f.add(listFile[i].getAbsolutePath());

            }
        }
    }

}
