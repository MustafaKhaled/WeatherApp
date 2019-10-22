package com.weatherapp.canvas.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.weatherapp.canvas.data.remote.model.WeatherResponseModel;
import com.weatherapp.canvas.data.repo.WeatherHistoryRepository;
import com.weatherapp.canvas.util.ResponseApi;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WeatherHistoryViewModel extends ViewModel {
    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<ResponseApi<File[]>> historyLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseApi<WeatherResponseModel>> weatherLiveData =  new MutableLiveData<>();
    private WeatherHistoryRepository repository;
    @Inject
    public WeatherHistoryViewModel(WeatherHistoryRepository repository) {
        this.repository = repository;
    }

    public void loadHistory(){
        disposable.add(repository.getAllFiles()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(result -> historyLiveData.postValue(ResponseApi.loading()))
                .subscribe(result -> historyLiveData.setValue(ResponseApi.success(result))
                        , throwable -> historyLiveData.setValue(ResponseApi.error(throwable))));
    }

    public void loadWeatherResponse(){
        disposable.add(repository.getWeather()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(result -> weatherLiveData.postValue(ResponseApi.loading()))
                .subscribe(result -> weatherLiveData.setValue(ResponseApi.success(result))
                        , throwable -> weatherLiveData.setValue(ResponseApi.error(throwable))));
    }

    public LiveData<ResponseApi<File[]>> getFilesHistory(){
        return historyLiveData;
    }

    public LiveData<ResponseApi<WeatherResponseModel>> getWeatherLiveData(){
        return weatherLiveData;
    }

}
