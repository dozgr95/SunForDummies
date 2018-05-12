package com.example.doz.sunfordummies.Business.Location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.security.ProviderException;
import java.util.ArrayList;
import java.util.List;

public class AndroidLocator implements Locator {
    private LocationManager locationManager;

    public AndroidLocator(final Context context) throws LocationPermissionException {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new LocationPermissionException();
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            throw new ProviderException();
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    public void addListener(LocationObserver observer) {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListenerAdapter(observer));
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
