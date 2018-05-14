package com.example.doz.sunfordummies.Business.SunData;

import android.widget.TextView;

import com.example.doz.sunfordummies.Business.Location.LocationDTO;
import com.example.doz.sunfordummies.Data.SunDataPersistenceManager;
import com.example.doz.sunfordummies.R;
import com.example.doz.sunfordummies.Utils.EmptySunDataDTO;
import com.example.doz.sunfordummies.Utils.SunDataDTO;

import java.util.Calendar;
import java.util.Date;

public class SunDataManager {
    private SunDataPersistenceManager persistenceManager;

    public SunDataManager(SunDataPersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    }

    public SunDataDTO getSunData(Date date, LocationDTO location){
        SunDataDTO data = persistenceManager.findById(date, location);

        if(data instanceof EmptySunDataDTO){
            data = new SunDataDTO();
            data.setDate(date);
            data.setLocation(location);
            //API call
            persistenceManager.saveSunInformation(data);
        }

        return data;
    }
}
