package com.example.doz.sunfordummies.Business.Location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.example.doz.sunfordummies.Utils.LocationDTO;

/**
 * Created by Doz on 02.05.2018.
 */

public class Locator {

    public LocationDTO getLocation(Activity activity){
        Intent service = new Intent(activity, LocationService.class);
        activity.startService(service);

        LocationServiceConnection serviceConnection = new LocationServiceConnection();
        activity.bindService(service, serviceConnection, Context.BIND_AUTO_CREATE);
        LocationService.LocationServiceBinder binder = serviceConnection.getBinder();
        Location location = binder.getLastKnownLocation();

        return new LocationDTO(location.getLatitude(), location.getLongitude()); //fix TEMPORARY
    }
}
