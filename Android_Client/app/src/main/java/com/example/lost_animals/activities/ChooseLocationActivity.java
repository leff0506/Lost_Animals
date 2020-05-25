package com.example.lost_animals.activities;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lost_animals.R;
import com.example.lost_animals.custom_views.GoogleMarkerAddition;
import com.example.lost_animals.data_base.DataBaseConnection;
import com.example.lost_animals.data_base.MarkerInfo;
import com.example.lost_animals.data_base.SharedPreferencesHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class ChooseLocationActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String description;
    public static String DESCRIPTION_KEY = "DESCRIPTION_KEY";
    public static String IMAGE_KEY = "IMAGE_KEY";
    public static String CAMERA_KEY = "CAMERA_KEY";
    public static String RESULT_CODE = "RESULT_CODE";
    private String image_im_base64;
    private MarkerInfo mCurrLocationMarker ;
    private Button back;
    private Button next;
    private Bitmap image;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private View.OnClickListener backOnCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra(RESULT_CODE, "again");
            setResult(RESULT_OK, intent);
            finish();
        }
    };
    private View.OnClickListener nextOnCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DataBaseConnection.addPost(description,image_im_base64,mCurrLocationMarker.getLatLng(),ChooseLocationActivity.this);
            MapsActivity.updatePosts();

            Intent intent = new Intent();
            intent.putExtra(RESULT_CODE, "exit");
            setResult(RESULT_OK, intent);
            finish();
//            getParent().finish();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        requestLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_choose_location);
        back = findViewById(R.id.bt_back_choose_location);
        back.setOnClickListener(backOnCLickListener);
        next = findViewById(R.id.bt_next_choose_location);
        next.setOnClickListener(nextOnCLickListener);
        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        Bundle extras = getIntent().getExtras();
        description = extras.getString(DESCRIPTION_KEY);
        boolean from_cam = extras.getBoolean(CAMERA_KEY);
        if(from_cam){
            image = (Bitmap)getIntent().getParcelableExtra(IMAGE_KEY);
        }else{
            Bitmap thumbnail = (BitmapFactory.decodeFile(extras.getString(IMAGE_KEY)));
            image = thumbnail;
        }

        image_im_base64 = DataBaseConnection.imageToBase64(image);
        mCurrLocationMarker = new MarkerInfo(image_im_base64,true);


        mapFragment.getMapAsync(this);
    }
    private void  requestLocationPermission(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getApplicationContext(), "Location permission is granted by user!", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_LONG).show();
                    }
                })
                .onSameThread()
                .check();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
//        }
//        else {
//            buildGoogleApiClient();
//            mMap.setMyLocationEnabled(true);
//        }
        mMap.setOnMarkerDragListener(dragListener);
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    GoogleMap.OnMarkerDragListener dragListener = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            mCurrLocationMarker.setLatLng(marker.getPosition());
        }
    };
    @Override
    public void onLocationChanged(Location location) {

        mMap.clear();
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mCurrLocationMarker.setLatLng(latLng);
        GoogleMarkerAddition.addMarker(mMap,mCurrLocationMarker,sharedPreferencesHelper);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }


    }

}
