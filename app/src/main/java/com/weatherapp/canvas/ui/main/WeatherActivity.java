package com.weatherapp.canvas.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.weatherapp.canvas.callback.OnHistoryItemListener;
import com.weatherapp.canvas.data.remote.model.WeatherResponseModel;
import com.weatherapp.canvas.ui.details.FullImageActivity;
import com.weatherapp.canvas.ui.main.adapter.WeatherHistoryAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.weatherapp.canvas.R;
import com.weatherapp.canvas.di.component.DaggerWeatherHistoryComponent;
import com.weatherapp.canvas.di.modules.context.ContextModule;
import com.weatherapp.canvas.di.modules.multibinding.DaggerViewModelFactory;
import com.weatherapp.canvas.util.FileHelper;
import com.weatherapp.canvas.util.ResponseApi;
import com.weatherapp.canvas.viewmodel.WeatherHistoryViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class WeatherActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, OnHistoryItemListener {
    private static final String TAG = "WeatherActivity";
    private String city, country;
    private double windSpeed;
    private WeatherHistoryAdapter adapter = new WeatherHistoryAdapter(this);
    private Uri photoURI;
    private File photoFile;
    private File resultFile;
    final int CAMERA_PICTURE_REQUEST = 1;
    final int CAMERA_RESULT = 2;
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
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setUpRecyclerView();

        viewModel = ViewModelProviders.of(this,factory).get(WeatherHistoryViewModel.class);
        viewModel.loadWeatherResponse();
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
                    country = response.data.getSys().getCountry();
                    city = response.data.getName();
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
                if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                    openCamera();
                } else {
                    requestCamera();
                }
                break;
        }
    }
    private void requestCamera() {
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, CAMERA_PICTURE_REQUEST, Manifest.permission.CAMERA)
                        .setRationale("This Permission is Required")
                        .setPositiveButtonText("Ok")
                        .setNegativeButtonText("Cancel")
                        .build());
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
                Log.d(TAG, "openCamera: WeatherHistoryItem size : " + photoFile.length() + "Filename :" + photoFile.getName());
                deleteFile(photoFile.getName());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_RESULT);
                photoFile.deleteOnExit();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

//        if (requestCode == CAMERA_PICTURE_REQUEST
//                && grantResults.length > 0
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            openCamera();
//        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        openCamera();
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
            addOverlayBanner(file);
            if (photoFile.exists()) {
                photoFile.delete();
            }
        }

    }
    private void addOverlayBanner(File file) {
        Single.just(file)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Bitmap bitmap = FileHelper.createBitmapFromFile(file.getPath());
                        Bitmap result = FileHelper.drawTextToBitmap(getApplicationContext(), bitmap, city,country,windSpeed);
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
        private void setUpRecyclerView() {
            historyRv.setAdapter(adapter);
            historyRv.setHasFixedSize(true);
            historyRv.setLayoutManager(new LinearLayoutManager(historyRv.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onClick(Uri uri) {
        Intent intent = new Intent(this, FullImageActivity.class);
        intent.putExtra("image",uri);
        startActivity(intent);
    }
}
