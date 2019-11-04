package com.example.sajak.hamroguide.Agencies;

public class AgenciesGetSet{
    private String id, name, location, image, contact;
    private String latitude, longitude;

    public AgenciesGetSet(String id, String name, String location, String latitude, String longitude, String image, String contact) {
        this.setId(id);
        this.setName(name);
        this.setLocation(location);
        this.setImage(image);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setContact(contact);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
