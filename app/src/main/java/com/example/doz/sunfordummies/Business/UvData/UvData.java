package com.example.doz.sunfordummies.Business.UvData;

import com.example.doz.sunfordummies.Utils.LocationDTO;
import com.example.doz.sunfordummies.Utils.UvDataDTO;

import java.util.Date;

/**
 * Created by Doz on 02.05.2018.
 */

public class UvData {

    public UvDataDTO getUvData(Date date, LocationDTO location){
        // check if there are persisted data for this date...
        // filename has date and location in it

        //if not: call openweathermap api
        return null;
    }
}
