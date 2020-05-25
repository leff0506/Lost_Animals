package com.example.lost_animals.data_base;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {


    @PrimaryKey
    private int id;
    private String url;
    private String description;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
