package com.google.sample.cloudvision;

public class Location {
    String name;
    String id;
    double lat;
    double lng;
    int count;
    int seat;

    public Location() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getCount() {
        return count;
    }

    public int getSeat() {
        return seat;
    }

    public Location(String name, String id, double lat, double lng, int count, int seat) {
        this.name = name;
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.count = count;
        this.seat = seat;
    }
}
