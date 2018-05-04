package com.example.doz.sunfordummies.Utils;

/**
 * Created by Doz on 02.05.2018.
 */

public class LocationDTO {
    private String city;
    private double longitude;
    private double latitude;

    public LocationDTO(double latitude, double longitude){
        //set city also?
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude(){
        return longitude;
    };

    public double getLatitude(){
        return latitude;
    };
}