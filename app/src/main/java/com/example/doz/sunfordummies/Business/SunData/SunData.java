package com.example.doz.sunfordummies.Business.SunData;

import com.example.doz.sunfordummies.Business.Location.LocationDTO;
import com.example.doz.sunfordummies.Utils.SunDataDTO;

import java.util.Date;

public class SunData {

    public SunDataDTO getSunData(Date date, LocationDTO location){
        // check if there are persisted data for this date...
        // filename has date and location in it

        //if not: call sonnenverlauf.de api

        return null;
    }
}
