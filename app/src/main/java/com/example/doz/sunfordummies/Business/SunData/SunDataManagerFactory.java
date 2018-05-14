package com.example.doz.sunfordummies.Business.SunData;

import android.content.Context;
import android.location.Geocoder;

import com.example.doz.sunfordummies.Data.SunDataPersistenceManager;
import com.example.doz.sunfordummies.Data.SunDataPersistenceManagerFactory;

import java.util.Locale;

public class SunDataManagerFactory {
    private static SunDataManager sunDataManager;

    public static SunDataManager getSunDataManager(Context context){
        if(sunDataManager == null) {
            SunDataPersistenceManager persistenceManager = SunDataPersistenceManagerFactory.getPersistenceManager(context);
            sunDataManager = new SunDataManager(persistenceManager);
        }

        return sunDataManager;
    }
}
