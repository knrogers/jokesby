package com.roguekingapps.jokesby.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Joke implements Parcelable {

    private String id;
    private String title;

    @SerializedName("selftext")
    private String body;

    @SerializedName("author")
    private String user;

    private String url;

    public Joke(String id, String title, String body, String user, String url) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.user = user;
        this.url = url;
    }

    protected Joke(Parcel in) {
        id = in.readString();
        title = in.readString();
        body = in.readString();
        user = in.readString();
        url = in.readString();
    }

    public static final Creator<Joke> CREATOR = new Creator<Joke>() {
        @Override
        public Joke createFromParcel(Parcel in) {
            return new Joke(in);
        }

        @Override
        public Joke[] newArray(int size) {
            return new Joke[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeString(user);
        parcel.writeString(url);
    }
}
