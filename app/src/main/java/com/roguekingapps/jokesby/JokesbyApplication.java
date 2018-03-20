package com.roguekingapps.jokesby;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.ads.MobileAds;
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
        MobileAds.initialize(this, "ca-app-pub-5067360240308225~9978612122");

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
