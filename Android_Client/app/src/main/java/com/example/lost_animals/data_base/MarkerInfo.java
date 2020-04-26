package com.example.lost_animals.data_base;

import com.google.android.gms.maps.model.LatLng;

public class MarkerInfo {
    LatLng latLng;
    String url;


    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getUrl() {
        return url;
    }

    public MarkerInfo(LatLng latLng, String url) {
        this.latLng=latLng;
        this.url = url;
    }
    public MarkerInfo(String url){
        this(new LatLng(0,0),url);
    }
}
