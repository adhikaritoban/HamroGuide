package com.example.sajak.hamroguide.Places;

import android.util.Log;

public class PlacesGetSet {
    private String id, location, place, image;
    private Double latitude, longitude;

    public PlacesGetSet(String id, String location, Double latitude, Double longitude, String place, String image) {
        this.setId(id);
        this.setLocation(location);
        this.setPlace(place);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setImage(image);

        Log.e("Check", "Get Set" );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
