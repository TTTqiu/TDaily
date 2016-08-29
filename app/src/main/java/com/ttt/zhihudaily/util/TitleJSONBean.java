package com.ttt.zhihudaily.util;

public class TitleJSONBean {
    private String date;
    private Stories[] stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Stories[] getStories() {
        return stories;
    }

    public void setStories(Stories[] stories) {
        this.stories = stories;
    }

    public class Stories{
        private String title;
        private String[] images;
        private int id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String[] getImages() {
            return images;
        }

        public void setImages(String[] images) {
            this.images = images;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
