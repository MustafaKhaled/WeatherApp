package com.weatherapp.canvas.data.repo;

import android.content.Context;
import android.os.Environment;

import com.weatherapp.canvas.BuildConfig;
import com.weatherapp.canvas.data.remote.ApiServices;
import com.weatherapp.canvas.data.remote.model.WeatherResponseModel;
import com.weatherapp.canvas.util.ResponseApi;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import io.reactivex.Observable;
import io.reactivex.Single;

public class WeatherHistoryRepository {
    private ApiServices apiServices;
    private Context context;
    @Inject
    public WeatherHistoryRepository(ApiServices apiServices,Context context) {
        this.apiServices = apiServices;
        this.context = context;
    }

    public Observable<File[]> getAllFiles() {
        ArrayList<String> f = new ArrayList<>();// list of file paths
        File[] listFile = new File[0];
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());

        if (file.isDirectory()) {
            listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                f.add(listFile[i].getAbsolutePath());
            }
        }
        return Observable.just(listFile);
    }

    public Single<WeatherResponseModel> getWeather(String country){
        return apiServices.getWeatherResponse(country, BuildConfig.API_KEY);
    }
}
