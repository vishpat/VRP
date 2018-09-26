package com.urbanbasket.vrp;

public class Location {

    private String address;

    private float latitude;

    private float longitude;

    public Location(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("%s: (%f, %f)", this.address, this.latitude, this.longitude);
    }
}
