package com.ttt.zhihudaily.entity;


import java.io.Serializable;

public class Title implements Serializable{

    private String name;
    private String image;
    private int id;

    public Title(String name,String image){
        this.name=name;
        this.image = image;
    }

    public Title(String name, String image, int id){
        this(name, image);
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
