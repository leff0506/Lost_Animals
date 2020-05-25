package com.example.lost_animals.data_base.local_database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.lost_animals.data_base.Post;

@Database(entities = {Post.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PostDao getPostDao();
}
