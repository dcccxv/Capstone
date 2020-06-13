package com.google.sample.cloudvision;

public class MarkerItem {
    double lat;
    double lon;
    String storeName;
    String id;
    int seat;
    int count;



    public MarkerItem(double lat, double lon, int seat, int count, String storeName, String id) {
        this.lat = lat;
        this.lon = lon;
        this.seat = seat;
        this.count = count;
        this.storeName = storeName;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int price) {
        this.seat = seat;
    }

    public String getstoreName() {
        return storeName;
    }

    public void setstoreNamet(String storeName) {
        this.storeName = storeName;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
