package com.weatherapp.canvas.viewmodel;

import androidx.lifecycle.ViewModel;

import com.weatherapp.canvas.data.repo.WeatherHistoryRepository;

import java.io.File;

import javax.inject.Inject;

public class WeatherHistoryViewModel extends ViewModel {
    private WeatherHistoryRepository repository;
    @Inject
    public WeatherHistoryViewModel(WeatherHistoryRepository repository) {
        this.repository = repository;
    }

    public File[] loadHistory(){
        return repository.getAllFiles();
    }


}
