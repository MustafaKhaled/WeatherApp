package com.weatherapp.canvas.data.remote.di;

import com.weatherapp.canvas.data.remote.ApiServices;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiServiceModule {
    @Provides
    ApiServices providesApiServices(Retrofit retrofit){
        return retrofit.create(ApiServices.class);
    }
}
