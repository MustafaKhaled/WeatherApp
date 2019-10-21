package com.weatherapp.canvas.viewmodel.di;

import androidx.lifecycle.ViewModel;

import com.weatherapp.canvas.data.remote.ApiServices;
import com.weatherapp.canvas.data.remote.di.ApiServiceModule;
import com.weatherapp.canvas.di.modules.multibinding.ViewModelFactoryModule;
import com.weatherapp.canvas.di.modules.multibinding.ViewModelKey;
import com.weatherapp.canvas.di.scope.ApplicationContextScope;
import com.weatherapp.canvas.viewmodel.WeatherHistoryViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
@Module(includes = {ApiServiceModule.class, ViewModelFactoryModule.class})
public abstract class WeatherHistoryViewModelFactory {
    @ApplicationContextScope
    @Binds
    @IntoMap
    @ViewModelKey(WeatherHistoryViewModel.class)
    abstract ViewModel weatherHistoryViewModel(WeatherHistoryViewModel weatherHistoryViewModel);
}
