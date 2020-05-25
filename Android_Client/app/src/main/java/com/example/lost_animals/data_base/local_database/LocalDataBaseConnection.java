package com.example.lost_animals.data_base.local_database;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.example.lost_animals.data_base.Post;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LocalDataBaseConnection {
    private static AppDatabase db;

    public static List<Post> getPosts(Context context) throws ExecutionException, InterruptedException {
        if (db == null) {
            db = Room.databaseBuilder(context, AppDatabase.class, "populus-database").build();
        }
        return new GetUsersAsyncTask().execute().get();
    }

    public static void addPost(Context context, Post post) throws ExecutionException, InterruptedException {
        if (db == null) {
            db = Room.databaseBuilder(context, AppDatabase.class, "populus-database").build();
        }
        List<Post> posts = getPosts(context);
        boolean found= false;
        for(Post p : posts){
            if (p.getId() == post.getId())found = true;
        }
        if (found)return;
        new AgentAddTask((Activity) context, post, db).execute();
    }
    static class GetUsersAsyncTask extends AsyncTask<Void, Void, List<Post>> {

        @Override
        protected List<Post> doInBackground(Void... url) {
            return db.getPostDao().getAllPosts();
        }
    }
}

class AgentAddTask extends AsyncTask<Void, Void, Void> {

    //Prevent leak
    private WeakReference<Activity> weakActivity;
    private Post post;
    private AppDatabase db;

    public AgentAddTask(Activity activity, Post post, AppDatabase db) {
        weakActivity = new WeakReference<>(activity);
        this.post = post;
        this.db = db;
    }

    @Override
    protected Void doInBackground(Void... params) {
        db.getPostDao().insertAll(post);
        return null;
    }

}



