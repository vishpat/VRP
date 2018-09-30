package com.urbanbasket.vrp;

public class Location {

    private String address;

    private double latitude;

    private double longitude;

    private Location(String address){
        this.address = address;
    }

    public static Location getInstance(String address) {
        return new Location(address);
    }

    public String getAddress() {
        return this.address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("%s: (%f, %f)", this.address, this.latitude, this.longitude);
    }
}
