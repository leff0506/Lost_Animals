package com.example.lost_animals.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lost_animals.R;
import com.example.lost_animals.custom_views.BitmapTransformation;
import com.example.lost_animals.custom_views.GoogleMarkerAddition;
import com.example.lost_animals.data_base.DataBaseConnection;
import com.example.lost_animals.data_base.MarkerInfo;
import com.example.lost_animals.data_base.Post;
import com.example.lost_animals.data_base.local_database.LocalDataBaseConnection;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class PostActivity extends AppCompatActivity {
    public static String DESCRIPTION_KEY = "DESCRIPTION_KEY";
    public static String IMAGE_KEY = "IMAGE_KEY";
    public static String ID_KEY = "ID_KEY";
    public static String FAVOURITE_VISIBLE_KEY = "FAVOURITE_VISIBLE_KEY";
    private static String TAG = "PostActivity";
    private Button back;
    private ImageView image;
    private TextView textView;
    private Button addFavourite;
    private int id;
    private String url;
    private String description;
    private View.OnClickListener onBackClickListen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    private View.OnClickListener onAddToFavouritesClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Post post = new Post();
            post.setDescription(description);
            post.setId(id);
            post.setUrl(url);
            try {
                LocalDataBaseConnection.addPost(PostActivity.this,post);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);

        back = findViewById(R.id.post_activity_back);
        back.setOnClickListener(onBackClickListen);

        image = findViewById(R.id.post_activity_post_image);
        textView = findViewById(R.id.post_activity_description);

        Intent intent = getIntent();
        url =intent.getStringExtra(IMAGE_KEY);
        description = intent.getStringExtra(DESCRIPTION_KEY);
        id = intent.getIntExtra(ID_KEY,0);
        boolean addFavouriteVisible = intent.getBooleanExtra(FAVOURITE_VISIBLE_KEY,false);

        textView.setText(description);
        addFavourite = findViewById(R.id.post_activity_add_to_favourites);
        addFavourite.setOnClickListener(onAddToFavouritesClickListener);

        if(!addFavouriteVisible){
            addFavourite.setVisibility(View.GONE);
        }
        if (url.startsWith("http")){
            new PostImageAddition().execute(url);
        }else{
            image.setImageBitmap(DataBaseConnection.base64ToImage(url));
        }
    }
    public class PostImageAddition extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;
        public PostImageAddition(  ){

        }
        @Override
        protected Bitmap doInBackground(String... url) {
            Log.i(TAG,"Download image.");
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

            image.setImageBitmap(bitmap);
        }



    }
}
