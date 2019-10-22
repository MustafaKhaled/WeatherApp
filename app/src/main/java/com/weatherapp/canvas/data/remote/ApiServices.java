package com.weatherapp.canvas.data.remote;

import com.weatherapp.canvas.data.remote.model.WeatherResponseModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("weather/")
    Single<WeatherResponseModel> getWeatherResponse(@Query("q") String query, @Query("APPID") String apiKey);
}
