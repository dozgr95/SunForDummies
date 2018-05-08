package com.example.doz.sunfordummies.Business.Location;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class LocationServiceConnection implements ServiceConnection {
    private LocationService.LocationServiceBinder binder;


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.binder = (LocationService.LocationServiceBinder)service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.binder = null;
    }

    public LocationService.LocationServiceBinder getBinder() {
        return binder;
    }
}
