package com.weatherapp.canvas.di.component;

import com.weatherapp.canvas.di.modules.NetModule.RetrofitModule;
import com.weatherapp.canvas.di.modules.context.ContextModule;
import com.weatherapp.canvas.di.modules.multibinding.ViewModelFactoryModule;
import com.weatherapp.canvas.di.scope.ApplicationContextScope;
import com.weatherapp.canvas.ui.main.WeatherActivity;
import com.weatherapp.canvas.viewmodel.di.WeatherHistoryViewModelFactory;

import dagger.Component;

@ApplicationContextScope
@Component(modules = {ContextModule.class, RetrofitModule.class, ViewModelFactoryModule.class, WeatherHistoryViewModelFactory.class})
public interface WeatherHistoryComponent {

    void inject(WeatherActivity weatherActivity);
}
