package com.example.sajak.hamroguide.news;

public class NewsGetSet {
    private String id, head, content, date, writer, image, website, imagesec, imagethird;

    public NewsGetSet(String id, String head, String content, String date, String writer, String image, String website, String imagesec, String imagethird) {
        this.setId(id);
        this.setHead(head);
        this.setContent(content);
        this.setDate(date);
        this.setWriter(writer);
        this.setImage(image);
        this.setWebsite(website);
        this.setImagesec(imagesec);
        this.setImage(imagethird);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImagesec() {
        return imagesec;
    }

    public void setImagesec(String imagesec) {
        this.imagesec = imagesec;
    }

    public String getImagethird() {
        return imagethird;
    }

    public void setImagethird(String imagethird) {
        this.imagethird = imagethird;
    }
}
