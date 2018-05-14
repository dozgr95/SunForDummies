package com.example.doz.sunfordummies.Business.Location;

import android.annotation.SuppressLint;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Locale;

public class AndroidLocator implements Locator {
    private LocationManager locationManager;

    public AndroidLocator(final LocationManager locationManager)  {
        this.locationManager = locationManager;
    }

    @Override
    @SuppressLint("MissingPermission")
    public void addListener(LocationObserver observer) {
        LocationListenerAdapter locationListener = new LocationListenerAdapter(observer);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, locationListener);
        locationListener.onLocationChanged(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
    }

    @Override
    public void removeListener(LocationObserver observer) {
        locationManager.removeUpdates(new LocationListenerAdapter(observer));
    }

    private class LocationListenerAdapter implements LocationListener {
        private LocationObserver observer;

        public LocationListenerAdapter(LocationObserver locationObserver){
            observer = locationObserver;
        }

        @Override
        public void onLocationChanged(Location location) {
            LocationDTO dto = new LocationDTO(location.getLatitude(), location.getLongitude());
            observer.update(dto);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
