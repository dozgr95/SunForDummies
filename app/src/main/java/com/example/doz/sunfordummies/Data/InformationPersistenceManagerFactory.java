package com.example.doz.sunfordummies.Data;

import android.content.Context;

public class InformationPersistenceManagerFactory {
    public static InformationPersistenceManager getPersistenceManager(Context context) throws Exception {
        /*if(context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            throw new Exception();
        }
        context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);*/
        return new InformationPersistenceManagerJSON(context.getFilesDir());
    }
}
