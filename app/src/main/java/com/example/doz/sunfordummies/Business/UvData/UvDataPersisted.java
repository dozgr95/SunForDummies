package com.example.doz.sunfordummies.Business.UvData;

import android.os.AsyncTask;
import android.util.Log;

public class UvDataPersisted extends AsyncTask<String, String, String> {
    private UvData uvData;
    public UvDataPersisted(UvData uvData){
        this.uvData = uvData;
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    protected void onPostExecute(String uvValue) {
        try{
            double uv = Double.valueOf(uvValue);
            uvData.setUvData(uv);
        } catch (Exception e){
            Log.i("DummyData", "uv could not be read");
        }
    }
}
