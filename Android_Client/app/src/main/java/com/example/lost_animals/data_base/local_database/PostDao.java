package com.example.lost_animals.data_base.local_database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.lost_animals.data_base.Post;

import java.util.List;

@Dao
public interface PostDao {
    // Добавление Person в бд
    @Insert
    void insertAll(Post... posts);

    // Удаление Person из бд
    @Delete
    void delete(Post post);

    // Получение всех Person из бд
    @Query("SELECT * FROM post")
    List<Post> getAllPosts();

}
