package ch.hslu.mobpro.sunfordummies.Data;

import android.content.Context;

public class SunDataPersistenceManagerFactory {
    public static SunDataPersistenceManager getPersistenceManager(Context context) {
        /*if(context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            throw new Exception();
        }
        context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);*/
        return new SunDataPersistenceManagerJSON(context.getFilesDir());
    }
}
