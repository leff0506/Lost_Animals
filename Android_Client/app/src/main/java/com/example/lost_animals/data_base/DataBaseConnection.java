package com.example.lost_animals.data_base;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DataBaseConnection {
    public static List<MarkerInfo> getMarkerUrls(){
        List<MarkerInfo> result = new ArrayList<>();
        result.add(new MarkerInfo(new LatLng(46.4926819,30.7186443),"https://smaller-pictures.appspot.com/images/dreamstime_xxl_65780868_small.jpg"));
        result.add(new MarkerInfo(new LatLng(46.5026819,30.7186443),"https://smaller-pictures.appspot.com/images/dreamstime_xxl_65780868_small.jpg"));
        return result;
    }
}
