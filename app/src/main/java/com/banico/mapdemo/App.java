package com.banico.mapdemo;

import android.app.Application;
import com.mapbox.mapboxsdk.Mapbox;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Mapbox.getInstance(getApplicationContext(), "pk.eyJ1IjoieWFzYW1hbmkiLCJhIjoiY2szbGFpbWlwMDUxczNkczM2eGtjZ3pjaSJ9.5EKXazzgnvCpIS2L2SzqFA");
    }
}
