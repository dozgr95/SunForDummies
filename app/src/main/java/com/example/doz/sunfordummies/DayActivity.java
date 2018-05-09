package com.example.doz.sunfordummies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.doz.sunfordummies.Business.UvData.UvData;
import com.example.doz.sunfordummies.Utils.LocationDTO;
import com.example.doz.sunfordummies.Business.Location.Locator;

import java.util.Date;

public class DayActivity extends AppCompatActivity {
    private Date targetDate;
    private Locator locator = new Locator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);

        //get Date
        targetDate = new Date();
        //get Location
        LocationDTO currentLocation = locator.getLocation(this);

        //get Data from Modules
        //SunDataDTO sundata = new SunData().getSunData(this.targetDate, currentLocation);
        new UvData().getUvData(this.targetDate, currentLocation, this);

        // private setInfo() //static info text (disclaimer etc)
    }
}
