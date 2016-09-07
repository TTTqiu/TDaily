package com.ttt.zhihudaily.entity;

public class TitleBean {
    private String date;
    private Stories[] stories;
    private TopStories[] top_stories;

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

    public TopStories[] getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(TopStories[] top_stories) {
        this.top_stories = top_stories;
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

    public class TopStories{
        private String title;
        private String image;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
