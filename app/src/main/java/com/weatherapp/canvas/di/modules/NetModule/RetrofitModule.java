package com.weatherapp.canvas.di.modules.NetModule;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weatherapp.canvas.di.scope.ApplicationContextScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module(includes = OkHttpClientModule.class)
public class RetrofitModule {
    public static final String BASE_URL = "https://google.com";
    @ApplicationContextScope
    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient, Gson gson, GsonConverterFactory gsonConverterFactory){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build();
    }
    @ApplicationContextScope
    @Provides
    public Gson providesGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }
    @ApplicationContextScope
    @Provides
    public GsonConverterFactory providesGsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }
}
