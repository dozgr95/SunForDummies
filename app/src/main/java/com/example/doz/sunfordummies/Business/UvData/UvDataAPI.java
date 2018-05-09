package com.example.doz.sunfordummies.Business.UvData;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UvDataAPI extends AsyncTask<String, String, String> {
    private UvData uvData;
    public UvDataAPI(UvData uvData){
        this.uvData = uvData;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL request;
        String uvValue = "";
        try {
            request = new URL(strings[0]);
            HttpURLConnection httpConnection = (HttpURLConnection) request.openConnection();
            httpConnection.connect();
            if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream stream = httpConnection.getInputStream();
                StringBuilder text= new StringBuilder();
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String json = reader.readLine();
                JSONObject jsonobject = new JSONObject(json);
                uvValue = jsonobject.optString("value");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uvValue;
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
