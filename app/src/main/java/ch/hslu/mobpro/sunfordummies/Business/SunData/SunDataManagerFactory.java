package ch.hslu.mobpro.sunfordummies.Business.SunData;

import android.content.Context;

import ch.hslu.mobpro.sunfordummies.Data.SunDataPersistenceManager;
import ch.hslu.mobpro.sunfordummies.Data.SunDataPersistenceManagerFactory;

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
