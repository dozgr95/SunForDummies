package com.example.doz.sunfordummies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.doz.sunfordummies.Business.SunData.SunData;
import com.example.doz.sunfordummies.Business.UvData.UvData;
import com.example.doz.sunfordummies.Utils.LocationDTO;
import com.example.doz.sunfordummies.Business.Location.Locator;
import com.example.doz.sunfordummies.Utils.SunDataDTO;
import com.example.doz.sunfordummies.Utils.UvDataDTO;

import java.util.Date;

public class DayActivity extends AppCompatActivity {
    private Date targetDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);

        //get Date


        //get Location
        LocationDTO currentLocation = new Locator().getLocation();

        //get Data from Modules
        SunDataDTO sundata = new SunData().getSunData(this.targetDate, currentLocation);
        UvDataDTO uvdata = new UvData().getUvData(this.targetDate, currentLocation);

    }
}
