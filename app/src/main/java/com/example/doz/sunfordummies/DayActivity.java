package com.example.doz.sunfordummies;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.doz.sunfordummies.Business.Location.AndroidLocator;
import com.example.doz.sunfordummies.Business.Location.LocationDTO;
import com.example.doz.sunfordummies.Business.Location.LocationObserver;
import com.example.doz.sunfordummies.Business.Location.LocationPermissionException;
import com.example.doz.sunfordummies.Business.Location.Locator;
import com.example.doz.sunfordummies.Business.Location.LocatorFactory;

import java.security.ProviderException;
import java.util.Date;

public class DayActivity extends AppCompatActivity implements LocationObserver {
    private static final int LOCATION_PERMISSION_REQUEST = 24;
    private Locator locator;
    private Date targetDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);

        try {
            registerLocationObserver();
        } catch (LocationPermissionException e) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, LOCATION_PERMISSION_REQUEST);
        } catch (ProviderException e){
            //inform user
        }

        //get Date
        targetDate = new Date();
        //get Data from Modules
        //SunDataDTO sundata = new SunData().getSunData(this.targetDate, currentLocation);
        //new UvData().getUvData(this.targetDate, currentLocation, this);

        // private setInfo() //static info text (disclaimer etc)
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try {
                        registerLocationObserver();
                    } catch (LocationPermissionException e) {
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void registerLocationObserver() throws LocationPermissionException, ProviderException {
        locator = LocatorFactory.createLocator(this);
        locator.addListener(this);
    }

    @Override
    public void update(final LocationDTO location) {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.e("locator", "update received");
                TextView dateCityTextView = findViewById(R.id.txtDateCity);
                dateCityTextView.setText("Lat:" + location.getLatitude() + "Long:" + location.getLongitude());
            }
        });
    }

    @Override
    protected void onDestroy() {
        locator.removeListener(this);
        super.onDestroy();
    }
}
