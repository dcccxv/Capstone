package com.google.sample.cloudvision;

public class Location {
    String name = "";
    String id = "";
    String shopimgUrl = "";
    double lat;
    double lng;
    int count;
    int seat;
    double time;

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

    public void setTime(double time) {
        this.time = time;
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

    public double getTime() {
        return time;
    }

    public Location(String name, String id, String shopimgUrl, double lat, double lng, int count, int seat, double time) {
        this.name = name;
        this.id = id;
        this.shopimgUrl = shopimgUrl;
        this.lat = lat;
        this.lng = lng;
        this.count = count;
        this.seat = seat;
        this.time = time;
    }

    public void setShopimgUrl(String shopimgUrl) {
        this.shopimgUrl = shopimgUrl;
    }

    public String getShopimgUrl() {
        return shopimgUrl;
    }

}
