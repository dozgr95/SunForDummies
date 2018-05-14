package com.example.doz.sunfordummies.Business.Location;

public class LocationDTO {
    private String city;
    private double longitude;
    private double latitude;

    public LocationDTO(){
    }

    public LocationDTO(double latitude, double longitude){
        //set city also?
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
