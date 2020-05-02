package com.example.lost_animals.custom_views;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.lost_animals.data_base.MarkerInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;

public class GoogleMarkerAddition {
    public static void addMarker(GoogleMap map, MarkerInfo markerInfo){
        new GoogleMarkerAddition.GetImageFromUrl(map,markerInfo).execute(markerInfo.getUrl());
    }
    public static class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        GoogleMap map;
        Bitmap bitmap;
        MarkerInfo markerInfo;
        public GetImageFromUrl(  GoogleMap map, MarkerInfo markerInfo){
            this.map = map;
            this.markerInfo =markerInfo;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            if(stringUrl == null){
                return null;
            }
            bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(markerInfo.getLatLng());
            if(bitmap == null){
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                map.addMarker(markerOptions);
                return;
            }
            int height = 100;
            int width = 100;
            Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);
            smallMarker = BitmapTransformation.createRoundedRectBitmap(smallMarker,50,50,50,50);
            smallMarker = BitmapTransformation.getRoundedCornerBitmap(smallMarker, Color.MAGENTA,4);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            markerOptions.draggable(markerInfo.isDraggable());
            map.addMarker(markerOptions);
        }



    }
}


