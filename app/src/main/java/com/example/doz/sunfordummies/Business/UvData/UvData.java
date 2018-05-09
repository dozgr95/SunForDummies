package com.example.doz.sunfordummies.Business.UvData;

import android.app.Activity;
import android.util.Log;

import com.example.doz.sunfordummies.Utils.LocationDTO;

import java.util.Date;

public class UvData {
    private final String KEY = "68e0ca9dbc72500e3c5d8ea0c24d6306";
    private String url = "http://api.openweathermap.org/data/2.5/uvi?appid=";
    public void getUvData(Date date, LocationDTO location, Activity activity){
        // check if there are persisted data for this date...
        // filename has date and location in it
        // Asnyc

        //if not: call openweathermap api
        this.url = this.url + KEY;
        this.url = this.url + "&lat="+ location.getLatitude() +"&lon=" + location.getLongitude();

        new UvDataAPI(this).execute(this.url); //Async call
    }

    protected void setUvData(double uvValue){
            // set view element uv-index (with color)

            // set view element sunburn potential
                //  uv < 3 means  low
                //  uv > 3  && uv < 6  means medium
                //  uv > 6 && uv < 9 means high
                // uv > 10 means very high



        Log.i("DummyData", String.valueOf(uvValue));
    }
}
