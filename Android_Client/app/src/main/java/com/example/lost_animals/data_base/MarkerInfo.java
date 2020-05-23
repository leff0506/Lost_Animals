package com.example.lost_animals.data_base;

import com.google.android.gms.maps.model.LatLng;

public class MarkerInfo {
    private LatLng latLng;
    private String url;

    public boolean isDraggable() {
        return draggable;
    }

    private boolean draggable;

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getUrl() {
        return url;
    }

    public MarkerInfo(LatLng latLng, String url,boolean draggable) {
        this.latLng=latLng;
        this.url = url;
        this.draggable = draggable;
    }
    public MarkerInfo(String url,boolean gradable){
        this(new LatLng(0,0),url,gradable);
    }
}
