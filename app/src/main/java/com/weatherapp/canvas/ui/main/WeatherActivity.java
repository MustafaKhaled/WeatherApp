package com.weatherapp.canvas.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.weatherapp.canvas.callback.OnHistoryItemListener;
import com.weatherapp.canvas.ui.details.FullImageActivity;
import com.weatherapp.canvas.ui.main.adapter.WeatherHistoryAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.weatherapp.canvas.R;
import com.weatherapp.canvas.di.component.DaggerWeatherHistoryComponent;
import com.weatherapp.canvas.di.modules.context.ContextModule;
import com.weatherapp.canvas.di.modules.multibinding.DaggerViewModelFactory;
import com.weatherapp.canvas.util.FileHelper;
import com.weatherapp.canvas.viewmodel.WeatherHistoryViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class WeatherActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, OnHistoryItemListener {
    private String  country;
    private double windSpeed;
    private WeatherHistoryAdapter adapter = new WeatherHistoryAdapter(this);
    private Uri photoURI;
    private File photoFile;
    private File resultFile;
    private static final int CAMERA_LOCATION_PICTURE_REQUEST = 1;
    private static final int CAMERA_RESULT = 2;
    WeatherHistoryViewModel viewModel;
    @Inject
    DaggerViewModelFactory factory;
    @BindView(R.id.fab)
    FloatingActionButton cameraBtn;
    @BindView(R.id.history_list)
    RecyclerView historyRv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = findViewById(R.id.toolbar);
        DaggerWeatherHistoryComponent.builder().contextModule(new ContextModule(getApplicationContext())).build().inject(this);
        viewModel = ViewModelProviders.of(this,factory).get(WeatherHistoryViewModel.class);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setUpRecyclerView();

        if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION))
            requestPermissionsRequired();
        else
            viewModel.loadWeatherResponse(determineLocation());

        viewModel.loadHistory();

        observeWeather();
        observeHistory();

    }


    private void observeHistory() {
        viewModel.getFilesHistory().observe(this,response -> {
            switch (response.status){
                case LOADING:
                    break;
                case SUCCESS:
                    adapter.addAll(response.data);
                    break;
                case ERROR:
                    break;
            }

        });
    }

    private void observeWeather() {
        viewModel.getWeatherLiveData().observe(this, response -> {
            switch (response.status){
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    cameraBtn.setEnabled(false);
                    return;

                case SUCCESS:
                    windSpeed = response.data.getWind().getSpeed();
                    cameraBtn.setEnabled(true);
                    break;

                case ERROR:
                    Snackbar.make(cameraBtn,getResources().getString(R.string.error_msg),Snackbar.LENGTH_LONG).show();
                    break;
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    @OnClick(R.id.fab)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION))
                    openCamera();
                else
                    requestPermissionsRequired();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION)){
            String country = determineLocation();
            viewModel.loadWeatherResponse(country);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_RESULT &&
                resultCode == RESULT_OK
        ) {
            File file = FileHelper.getFileFromUri(getApplicationContext(), photoURI);
            addOverlayBanner(file);
            if (photoFile.exists()) {
                photoFile.delete();
            }
        }

    }


    @Override
    public void onClick(Uri uri) {
        Intent intent = new Intent(this, FullImageActivity.class);
        intent.putExtra("image",uri);
        startActivity(intent);
    }


    private void setUpRecyclerView() {
        historyRv.setAdapter(adapter);
        historyRv.setHasFixedSize(true);
        historyRv.setLayoutManager(new LinearLayoutManager(historyRv.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void addOverlayBanner(File file) {
        Single.just(file)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Bitmap bitmap = FileHelper.createBitmapFromFile(file.getPath());
                        Bitmap result = FileHelper.drawTextToBitmap(getApplicationContext(), bitmap, country,windSpeed);
                        try {
                            resultFile = FileHelper.createFileFromBitmap(result, getApplicationContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onSuccess(File file) {
                        adapter.add(resultFile);
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = FileHelper.createImageFile(this);

            } catch (IOException ex) {
            }

            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.weather.canvas.fileprovider",
                        photoFile);
                deleteFile(photoFile.getName());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_RESULT);
                photoFile.deleteOnExit();
            }
        }
    }

    private void requestPermissionsRequired() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, CAMERA_LOCATION_PICTURE_REQUEST, perms)
                        .setRationale("This Permission is Required")
                        .setPositiveButtonText("Ok")
                        .setNegativeButtonText("Cancel")
                        .build());
    }


    @SuppressLint("MissingPermission")
    private String  determineLocation(){
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if( !lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            new AlertDialog.Builder(this)
                    .setTitle("Location service Off")
                    .setMessage("Please enable Location")
                    .setPositiveButton("Yes", (dialogInterface, i) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("No", null)
                    .show();

            return null;
        }
        else {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            return getLocationName(latitude,longitude);
        }

    }

    private String getLocationName(double lattitude, double longitude) {

        String countryName = "Not Found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(lattitude, longitude,
                    10);
            for (Address adrs : addresses) {
                if (adrs != null) {
                    countryName = adrs.getCountryName();
                    if (countryName != null && !countryName.equals("")) {
                        country = countryName;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countryName;

    }
}
