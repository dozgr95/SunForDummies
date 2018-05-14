package com.example.doz.sunfordummies.Business.SunData;

import com.example.doz.sunfordummies.Business.Location.LocationDTO;
import com.example.doz.sunfordummies.Data.SunDataPersistenceManager;
import com.example.doz.sunfordummies.Utils.EmptySunDataDTO;
import com.example.doz.sunfordummies.Utils.SunDataDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class SunDataManager {
    private SunDataPersistenceManager persistenceManager;

    public SunDataManager(SunDataPersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    }

    public SunDataDTO getSunData(LocalDate date, LocationDTO location){
        SunDataDTO data = persistenceManager.findById(date, location.getCity());

        if(data instanceof EmptySunDataDTO){
            data = new SunDataDTO();
            data.setDate(date);
            data.setCity(location.getCity());
            data.setSunrise(LocalTime.now());
            data.setSunset(LocalTime.now());
            data.setMaxPosition(35);
            data.setAbove(5);
            data.setEnergy(8.9);
            data.setSunburn("medium");
            data.setUv(2);
            data.setVitamin("medium");
            //API call
            persistenceManager.saveSunInformation(data);
        }

        return data;
    }
}
