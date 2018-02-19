package com.roguekingapps.jokesby;

import android.app.Application;
import android.content.Context;

import com.roguekingapps.jokesby.data.network.ApiHelper;
import com.roguekingapps.jokesby.di.component.ApplicationComponent;
import com.roguekingapps.jokesby.di.component.DaggerApplicationComponent;
import com.roguekingapps.jokesby.di.module.ApplicationModule;

import javax.inject.Inject;

public class JokesbyApplication extends Application {

    protected ApplicationComponent applicationComponent;

    @Inject
    ApiHelper apiHelper;

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
