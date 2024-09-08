package com.example.notificationmusicmanager;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String single;
    private String image;
    private String resource;

    public Song(String title, String single, String image, String resource) {
        this.title = title;
        this.single = single;
        this.image = image;
        this.resource = resource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
