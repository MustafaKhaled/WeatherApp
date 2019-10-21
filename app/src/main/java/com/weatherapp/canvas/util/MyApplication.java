package com.weatherapp.canvas.util;

import android.app.Application;
import android.content.Context;

import com.weatherapp.canvas.di.component.DaggerWeatherHistoryComponent;
import com.weatherapp.canvas.di.component.WeatherHistoryComponent;
import com.weatherapp.canvas.di.modules.NetModule.OkHttpClientModule;
import com.weatherapp.canvas.di.modules.NetModule.RetrofitModule;
import com.weatherapp.canvas.di.modules.context.ContextModule;

public class MyApplication extends Application {
    WeatherHistoryComponent weatherHistoryComponent;
    public static MyApplication myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        weatherHistoryComponent = DaggerWeatherHistoryComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .okHttpClientModule(new OkHttpClientModule())
                .retrofitModule(new RetrofitModule())
                .build();
    }

    public WeatherHistoryComponent weatherHistoryComponent(){
        return weatherHistoryComponent;
    }
    public static Context getPublicContext(){
        return myApp.getApplicationContext();
    }
}
