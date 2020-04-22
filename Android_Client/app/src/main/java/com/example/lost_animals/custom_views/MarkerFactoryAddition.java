package com.example.lost_animals.custom_views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.icu.text.CaseMap;
import android.os.HandlerThread;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class MarkerFactoryAddition {
    private List<Target> targets = new ArrayList<>();

    public MarkerFactoryAddition(final GoogleMap mMap, final String title, final String url, final LatLng latLng ) {



//        Target target = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                Log.d("Error","Error");
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title(title);
//                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//                mMap.addMarker(markerOptions);
//            }
//
//            @Override
//            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        };
//        targets.add(target);
//        Picasso .get().load(url)
////                .resize(100,100)
////                .centerCrop()
////                .transform(new BubbleTransformation(20))
//                .into(target);
    }


}


