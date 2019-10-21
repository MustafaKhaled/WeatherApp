package com.weatherapp.canvas.data.local.model;

import android.net.Uri;

import java.io.File;

public class WeatherHistoryItem {
    private File imageUri;
    private String city;
    private String dateCreated;

    public WeatherHistoryItem(File imageUri, String city, String dateCreated) {
        this.imageUri = imageUri;
        this.city = city;
        this.dateCreated = dateCreated;
    }

    public File getImageUri() {
        return imageUri;
    }

    public void setImageUri(File imageUri) {
        this.imageUri = imageUri;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
