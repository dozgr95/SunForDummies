package ch.hslu.mobpro.sunfordummies.Business.Location;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;

public class AndroidLocator implements Locator {
    private LocationManager locationManager;
    private Geocoder geocoder;

    public AndroidLocator(final LocationManager locationManager, Geocoder geocoder)  {
        this.locationManager = locationManager;
        this.geocoder = geocoder;
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
            if(location != null) {
                LocationDTO dto = new LocationDTO(location.getLatitude(), location.getLongitude());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    dto.setCity(addresses.get(0).getLocality());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                observer.update(dto);
            }
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
