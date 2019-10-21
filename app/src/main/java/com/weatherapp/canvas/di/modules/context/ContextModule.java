package com.weatherapp.canvas.di.modules.context;

import android.content.Context;


import com.weatherapp.canvas.di.scope.ApplicationContextScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    Context context;
    public ContextModule(Context context) {
        this.context = context;
    }
    @ApplicationContextScope
    @Provides
    Context providesContext(){
        return context;
    }
}
