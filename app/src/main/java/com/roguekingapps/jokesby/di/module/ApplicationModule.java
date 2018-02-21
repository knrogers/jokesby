package com.roguekingapps.jokesby.di.module;

import android.app.Application;
import android.content.Context;

import com.roguekingapps.jokesby.data.DataManager;
import com.roguekingapps.jokesby.data.DataManagerImpl;
import com.roguekingapps.jokesby.data.network.ApiHelper;
import com.roguekingapps.jokesby.data.network.ApiHelperImpl;
import com.roguekingapps.jokesby.di.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(DataManagerImpl dataManager) {
        return dataManager;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(ApiHelperImpl apiHelper) {
        return apiHelper;
    }
}
