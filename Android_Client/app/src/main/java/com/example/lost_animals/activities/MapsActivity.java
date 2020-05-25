package com.example.lost_animals.activities;


import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.lost_animals.R;
import com.example.lost_animals.custom_views.AddPostButtonListener;
import com.example.lost_animals.custom_views.GoogleMarkerAddition;
import com.example.lost_animals.data_base.DataBaseConnection;
import com.example.lost_animals.data_base.MarkerInfo;
import com.example.lost_animals.data_base.ServerSettings;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


import com.google.android.gms.location.LocationServices;

import android.location.Location;
import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.Marker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    private  static GoogleMap mMap;
    private Location mLastLocation;
    private static  MarkerInfo mCurrLocationMarker = new MarkerInfo("",false);
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Button add_post_button;
    private Button update_button;
    private Button favourites;
    private Button settings;
    private static boolean started = false;
    public static Context context;
    private static final String TAG = "MapsActivity";
    View.OnClickListener onAddPostClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestMultiplePermissions();
            selectImage(MapsActivity.this);
        }
    };
    View.OnClickListener onFavouritesOnCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MapsActivity.this,Favourites.class);
            startActivity(intent);
        }
    };
    View.OnClickListener onSettingsOnCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener onUpdateButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updatePosts();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.google_map);
        Log.i(TAG,"Trying to get all necessary permissions.");
        requestLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


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
        mMap.setOnMarkerClickListener(this);
//        }
//        else {
//            buildGoogleApiClient();
//            mMap.setMyLocationEnabled(true);
//        }


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
    protected void onResume() {
        super.onResume();
        if (!started){
            started = true;
        }else{
//            Toast.makeText(getApplicationContext(), "Hi you are back", Toast.LENGTH_SHORT).show();
            updatePosts();
        }

    }

    public static void updatePosts(){
        mMap.clear();
        DataBaseConnection.setMarkers(mMap,context,mCurrLocationMarker.getLatLng(),2000);

//        GoogleMarkerAddition.addMarker(mMap,mCurrLocationMarker);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurrLocationMarker.getLatLng()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG,"Location changed.");
        mMap.clear();
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mCurrLocationMarker.setLatLng(latLng);
//        DataBaseConnection.setMarkers(mMap,this,latLng,2000);
        updatePosts();
        // Make here function to update real market
//        GoogleMarkerAddition.addMarker(mMap,latLng,"https://smaller-pictures.appspot.com/images/dreamstime_xxl_65780868_small.jpg");



        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }


    }
    private void  requestLocationPermission(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.i(TAG,"Permissions are granted.");
//                            Toast.makeText(getApplicationContext(), "Location permission is granted by user!", Toast.LENGTH_SHORT).show();
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            add_post_button = findViewById(R.id.bt_add_post);
                            add_post_button.setOnClickListener(onAddPostClickListener);

                            update_button = findViewById(R.id.bt_update_posts);
                            update_button.setOnClickListener(onUpdateButtonClickListener);

                            favourites = findViewById(R.id.favourites);
                            favourites.setOnClickListener(onFavouritesOnCLickListener);

                            settings = findViewById(R.id.settings);
                            settings.setOnClickListener(onSettingsOnCLickListener);
                            context = MapsActivity.this;
                            Log.i(TAG,"Download the map.");
                            mapFragment.getMapAsync(MapsActivity.this);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
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
    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
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
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    private void selectImage(Context context) {
//        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        final CharSequence[] options = { "Take Photo","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Choose your profile picture");
        Log.i(TAG,"Selecting image.");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        Intent intent = new Intent(this,AddPostActivity.class);
                        intent.putExtra("KEY",AddPostActivity.START_BY_CAMERA);
                        intent.putExtra("Bitmap",selectedImage);
                        startActivity(intent);
//                        post_image.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();

                        Intent intent = new Intent(this,AddPostActivity.class);
                        intent.putExtra("KEY",AddPostActivity.START_BY_GALLERY);
                        intent.putExtra("Bitmap_uri",selectedImage);
                        startActivity(intent);
                    }
                    break;
                default:
                    finish();
                    throw new IllegalStateException("Unexpected value: " + requestCode);

            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (DataBaseConnection.markers.containsKey(marker)){

            Intent intent = new Intent(MapsActivity.this,PostActivity.class);
            MarkerInfo markerInfo = DataBaseConnection.markers.get(marker);
            intent.putExtra(PostActivity.IMAGE_KEY,markerInfo.getUrl());
            intent.putExtra(PostActivity.DESCRIPTION_KEY,markerInfo.getDescription());
            intent.putExtra(PostActivity.ID_KEY,markerInfo.getId());
            intent.putExtra(PostActivity.FAVOURITE_VISIBLE_KEY,true);
            Log.i(TAG,"Marker "+marker.getId()+" clicked.");
            startActivity(intent);
        }
        return false;
    }
}