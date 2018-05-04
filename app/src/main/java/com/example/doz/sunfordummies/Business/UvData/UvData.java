package com.example.doz.sunfordummies.Business.UvData;

import com.example.doz.sunfordummies.Utils.LocationDTO;
import com.example.doz.sunfordummies.Utils.UvDataDTO;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Doz on 02.05.2018.
 */

public class UvData {
    private final String KEY = "68e0ca9dbc72500e3c5d8ea0c24d6306";
    private String url = "http://api.openweathermap.org/data/2.5/uvi?appid=";
    public UvDataDTO getUvData(Date date, LocationDTO location){
        // check if there are persisted data for this date...
        // filename has date and location in it

        //if not: call openweathermap api
        this.url = this.url + KEY;
        this.url = this.url + "&lat="+ location.getLatitude() +"&lon=" + location.getLongitude();

        URL request = null;
        try {
            request = new URL(this.url);
            HttpURLConnection httpConnection = (HttpURLConnection) request.openConnection();
            httpConnection.connect();
                    if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        InputStream context = httpConnection.getInputStream();

                    }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
