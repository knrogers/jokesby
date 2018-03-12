package com.roguekingapps.jokesby.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roguekingapps.jokesby.data.network.PushShiftApi;
import com.roguekingapps.jokesby.data.network.RedditApi;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    private static final String BASE_PUSH_SHIFT_URL = "https://api.pushshift.io/";
    private static final String BASE_REDDIT_URL = "https://www.reddit.com/";

    @Provides
    @Singleton
    PushShiftApi providePushShiftApi() {
        return getPushShiftRetrofit().create(PushShiftApi.class);
    }

    @Provides
    @Singleton
    RedditApi provideRedditApi() {
        return getRedditRetrofit().create(RedditApi.class);
    }

    @Provides
    @Singleton
    Random provideRandom() {
        return new Random();
    }

    @Singleton
    private Retrofit getPushShiftRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_PUSH_SHIFT_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(getOkHttpClient())
                .build();
    }

    @Singleton
    private Retrofit getRedditRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_REDDIT_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
