package com.example.doz.sunfordummies.Business.SunData;

import android.content.Context;

import com.example.doz.sunfordummies.Data.SunDataPersistenceManager;
import com.example.doz.sunfordummies.Data.SunDataPersistenceManagerFactory;

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
