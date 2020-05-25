package com.example.lost_animals.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lost_animals.R;
import com.example.lost_animals.data_base.DataBaseConnection;
import com.example.lost_animals.data_base.Post;
import com.example.lost_animals.data_base.local_database.LocalDataBaseConnection;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Favourites extends AppCompatActivity {
    private Button back;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    public static String TAG ="Favourites";
    View.OnClickListener onBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        back = findViewById(R.id.favourites_back);
        back.setOnClickListener(onBackClickListener);

        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout2 = findViewById(R.id.linearLayout2);
        linearLayout3 = findViewById(R.id.linearLayout3);
        try {
            List<Post> posts = LocalDataBaseConnection.getPosts(this);
            LinearLayout[] layouts = new LinearLayout[]{linearLayout1,linearLayout2,linearLayout3};
            int cur =0;
            for (Post p : posts){
                ImageView imageView = new ImageView(this);
                if (p.getUrl().startsWith("http")){
                    new PostImageAddition(imageView).execute();
                }else{
                    imageView.setImageBitmap(DataBaseConnection.base64ToImage(p.getUrl()));
                }
                imageView.setOnClickListener(new CustomClickable(p,this));
                LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(vp);
                layouts[cur].addView(imageView);
                cur++;
                cur%=3;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public class PostImageAddition extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;
        ImageView imageView;
        public PostImageAddition(  ImageView imageView){
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            Log.i(TAG,"Post addition.");
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

            imageView.setImageBitmap(bitmap);
        }



    }
}
class CustomClickable implements View.OnClickListener{
    Post post;
    Activity activity;


    public CustomClickable(Post post, Activity activity) {
        this.post = post;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(activity,PostActivity.class);
        intent.putExtra(PostActivity.IMAGE_KEY,post.getUrl());
        intent.putExtra(PostActivity.DESCRIPTION_KEY,post.getDescription());
        intent.putExtra(PostActivity.ID_KEY,post.getId());
        Log.i(Favourites.TAG,"Post "+post.getId()+" opened.");
        intent.putExtra(PostActivity.FAVOURITE_VISIBLE_KEY,false);
        activity.startActivity(intent);
    }
}