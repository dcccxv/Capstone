package com.google.sample.cloudvision;

public class MarkerItem {
    double lat;
    double lon;
    String storeName;
    int seat;

    public MarkerItem(double lat, double lon, int seat, String storeName) {
        this.lat = lat;
        this.lon = lon;
        this.seat = seat;
        this.storeName = storeName;
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
}
