package com.example.doz.sunfordummies;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.doz.sunfordummies.Business.Location.LocationDTO;
import com.example.doz.sunfordummies.Business.Location.LocationObserver;
import com.example.doz.sunfordummies.Business.Location.LocationPermissionException;
import com.example.doz.sunfordummies.Business.Location.Locator;
import com.example.doz.sunfordummies.Business.Location.LocatorFactory;
import com.example.doz.sunfordummies.Business.SunData.SunDataManager;
import com.example.doz.sunfordummies.Business.SunData.SunDataManagerFactory;
import com.example.doz.sunfordummies.Utils.SunDataDTO;

import java.io.IOException;
import java.security.ProviderException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DayActivity extends AppCompatActivity implements LocationObserver {
    private static final int LOCATION_PERMISSION_REQUEST = 24;
    private Locator locator;
    private SunDataManager sunDataManager;
    private SunDataDTO currentSunData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);

        sunDataManager = SunDataManagerFactory.getSunDataManager(this);
        currentSunData = new SunDataDTO();
        currentSunData.setDate(new Date());

        try {
            registerLocationObserver();
        } catch (LocationPermissionException e) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, LOCATION_PERMISSION_REQUEST);
        } catch (ProviderException e){
            //inform user
        }

        //get Data from Modules
        //SunDataDTO sundata = new SunDataManager().getSunData(this.targetDate, currentLocation);
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
                        Log.e("sunfordummies", "Permission not received.");
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
        Date currentDate = currentSunData.getDate();
        currentSunData = sunDataManager.getSunData(currentDate, location);

        runOnUiThread(new Runnable() {
            public void run() {
                updateSunDataOnGUI();
            }
        });
    }

    private Date getDayBefore(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    @Override
    protected void onDestroy() {
        locator.removeListener(this);
        super.onDestroy();
    }

    public void onClickPrevious(View button) {
        Date currentDate = currentSunData.getDate();
        LocationDTO locationDTO = currentSunData.getLocation();

        currentSunData = sunDataManager.getSunData(getDayBefore(currentDate), locationDTO);
        updateSunDataOnGUI();
    }

    private void updateSunDataOnGUI(){
        TextView txtDateCity = findViewById(R.id.txtDateCity);
        TextView txtMaxPosition = findViewById(R.id.txtMaxPosition);
        TextView txtSunburn = findViewById(R.id.txtSunburn);
        TextView txtSunrise = findViewById(R.id.txtSunrise);
        TextView txtSunset = findViewById(R.id.txtSunset);
        TextView txtUV = findViewById(R.id.txtUV);
        TextView txtVitamin = findViewById(R.id.txtVitamin);
        TextView txtEnergy = findViewById(R.id.txtEnergy);

        LocationDTO locationDTO = currentSunData.getLocation();

        //txtMaxPosition.setText(sunDataDTO.ge);
        txtUV.setText(currentSunData.getUv());
    }
}
