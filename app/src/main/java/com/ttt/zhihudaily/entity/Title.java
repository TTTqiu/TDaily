package com.ttt.zhihudaily.entity;


import java.io.Serializable;

public class Title implements Serializable{

    private String name;
    private String image;
    private String date;
    private int isFavourite;
    private int id;

    public Title(String name, String image, int id){
        this.name=name;
        this.image=image;
        this.id=id;
    }

    public Title(String name,String image,int id,String date,int isFavourite){
        this(name,image,id);
        this.date = date;
        this.isFavourite = isFavourite;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    @Override
    public String toString() {
        return "Title{" +
                "date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", isFavourite=" + isFavourite +
                ", id=" + id +
                '}';
    }
}
