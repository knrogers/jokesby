package com.roguekingapps.jokesby.di.module;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.network.ApiHelper;
import com.roguekingapps.jokesby.data.network.ApiHelperImpl;
import com.roguekingapps.jokesby.data.network.JokeApi;
import com.roguekingapps.jokesby.di.ApplicationContext;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    ApiHelper provideApiHelper(ApiHelperImpl apiHelper) {
        return apiHelper;
    }

    @Provides
    @Singleton
    JokeApi provideJokeApi() {
        return getRetrofit().create(JokeApi.class);
    }

    @Singleton
    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(application.getString(R.string.base_api_url))
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(getOkHttpClient())
                .build();
    }

    @Singleton
    private Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    @Singleton
    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }
}
