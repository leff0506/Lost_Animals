package com.example.lost_animals.activities;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
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
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.lost_animals.R;
import com.example.lost_animals.custom_views.MarkerFactoryAddition;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;

import android.location.Location;
import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.MediaStore;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        Bitmap intenet = getBitmapFromURL("https://lh3.googleusercontent.com/proxy/HWTWep98JmfDDi9q-88DdakrKq6rhSi2bD25Uugpm9K4L5JopcnYcptTg0VPlj9sHiPIKzLjKw51jdHv0A9b4gzCXCD91Ie6c3clVJjIwCyiS2xmHduyp2-l0OaicwWFDrv-");
//        if(intenet == null){
//            System.out.println("Error");
//        }
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(intenet));
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        mCurrLocationMarker = mMap.addMarker(markerOptions);
//        MarkerFactoryAddition markerFactoryAddition =new MarkerFactoryAddition(mMap,"","https://lh3.googleusercontent.com/proxy/HWTWep98JmfDDi9q-88DdakrKq6rhSi2bD25Uugpm9K4L5JopcnYcptTg0VPlj9sHiPIKzLjKw51jdHv0A9b4gzCXCD91Ie6c3clVJjIwCyiS2xmHduyp2-l0OaicwWFDrv-",latLng);

        new GetImageFromUrl(mMap,latLng,this).execute("https://smaller-pictures.appspot.com/images/dreamstime_xxl_65780868_small.jpg");
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    public static class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        GoogleMap map;
        Bitmap bitmap;
        LatLng latLng;
        Context context;
        public GetImageFromUrl(  GoogleMap map, LatLng latLng,Context context){
            this.map = map;
            this.latLng =latLng;
            this.context = context;
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

            int height = 400;
            int width = 400;
            Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);
            smallMarker = createRoundedRectBitmap(smallMarker,50,50,50,50);
            smallMarker = getRoundedCornerBitmap1(smallMarker,Color.MAGENTA,50,50);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            map.addMarker(markerOptions);
        }

        public static Bitmap getRoundedCornerBitmap1(Bitmap bitmap, int color, int cornerDips, int borderDips) {


            Bitmap output = Bitmap.createBitmap(bitmap.getWidth()+2*borderDips,
                    bitmap.getHeight()+2*borderDips,
                    Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(output);

            canvas.drawColor(Color.TRANSPARENT);

            final RectF rectF = new RectF(0, 0, output.getWidth(), output.getHeight());
            final Paint paint = new Paint();
            // prepare canvas for transfer
            paint.setAntiAlias(true);
            paint.setStrokeWidth((float) borderDips);
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawRoundRect(rectF, borderDips, borderDips, paint);

            canvas.drawBitmap(bitmap, borderDips, borderDips, null);
            bitmap.recycle();
            return output;
        }
        private static Bitmap createRoundedRectBitmap(@NonNull Bitmap bitmap,
                                                      float topLeftCorner, float topRightCorner,
                                                      float bottomRightCorner, float bottomLeftCorner) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = Color.MAGENTA;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            Path path = new Path();
            float[] radii = new float[]{
                    topLeftCorner, bottomLeftCorner,
                    topRightCorner, topRightCorner,
                    bottomRightCorner, bottomRightCorner,
                    bottomLeftCorner, bottomLeftCorner
            };

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);

            paint.setColor(color);
            path.addRoundRect(rectF, radii, Path.Direction.CW);
            canvas.drawPath(path, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}