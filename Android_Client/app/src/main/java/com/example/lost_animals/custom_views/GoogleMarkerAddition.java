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
        new GoogleMarkerAddition.GetImageFromUrl(map,markerInfo.getLatLng()).execute(markerInfo.getUrl());
    }
    public static class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        GoogleMap map;
        Bitmap bitmap;
        LatLng latLng;
        public GetImageFromUrl(  GoogleMap map, LatLng latLng){
            this.map = map;
            this.latLng =latLng;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
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
            markerOptions.position(latLng);

            int height = 100;
            int width = 100;
            Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);
            smallMarker = BitmapTransformation.createRoundedRectBitmap(smallMarker,50,50,50,50);
            smallMarker = BitmapTransformation.getRoundedCornerBitmap(smallMarker, Color.MAGENTA,4);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            map.addMarker(markerOptions);
        }



    }
}


