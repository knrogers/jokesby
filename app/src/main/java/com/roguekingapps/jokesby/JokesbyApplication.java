package com.roguekingapps.jokesby;

import android.app.Application;
import android.content.Context;

import com.roguekingapps.jokesby.di.component.ApplicationComponent;
import com.roguekingapps.jokesby.di.component.DaggerApplicationComponent;
import com.roguekingapps.jokesby.di.module.ApplicationModule;

public class JokesbyApplication extends Application {

    protected ApplicationComponent applicationComponent;

    public static JokesbyApplication get(Context context) {
        return (JokesbyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent(){
        return applicationComponent;
    }
}
