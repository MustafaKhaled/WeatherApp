package com.weatherapp.canvas.data.repo;

import android.content.Context;
import android.os.Environment;

import com.weatherapp.canvas.data.remote.ApiServices;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Module;

public class WeatherHistoryRepository {
    private ApiServices apiServices;
    private Context context;
    @Inject
    public WeatherHistoryRepository(ApiServices apiServices,Context context) {
        this.apiServices = apiServices;
        this.context = context;
    }

    public File[] getAllFiles() {
        ArrayList<String> f = new ArrayList<>();// list of file paths
        File[] listFile = new File[0];
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());

        if (file.isDirectory()) {
            listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                f.add(listFile[i].getAbsolutePath());
            }
        }
        return listFile;
    }

    public ArrayList<String> getAllFileNames() {
        ArrayList<String> f = new ArrayList<>();// list of file paths
        String[] listNames = new String[0];
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());

        if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                f.add(listFile[i].getName());
            }
        }
        return f;
    }
}
