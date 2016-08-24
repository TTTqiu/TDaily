package com.ttt.zhihudaily;

public class Title {

    private String name;
    private int imageId;
    private int id;

    public Title(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
