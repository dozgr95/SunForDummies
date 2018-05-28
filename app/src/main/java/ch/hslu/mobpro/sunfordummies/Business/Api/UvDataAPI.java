package ch.hslu.mobpro.sunfordummies.Business.Api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.hslu.mobpro.sunfordummies.Utils.SunDataDTO;
import ch.hslu.mobpro.sunfordummies.Utils.UVConverter;

public class UvDataAPI extends AsyncTask<String, String, SunDataDTO> {
    private SunDataDTO sunDataDTO;

    public UvDataAPI(SunDataDTO sunDataDTO){
        this.sunDataDTO = sunDataDTO;
    }

    @Override
    protected SunDataDTO doInBackground(String... strings) {
        URL request;
        try {
            request = new URL(strings[0]);
            HttpURLConnection httpConnection = (HttpURLConnection) request.openConnection();
            httpConnection.connect();
            if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream stream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String json = reader.readLine();
                JSONObject jsonobject = new JSONObject(json);
                String uvValue = jsonobject.optString("value");
                setSunDataDTO(Double.parseDouble(uvValue));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sunDataDTO;
    }

    protected void setSunDataDTO(Double uvValue) {
        try{
            sunDataDTO.setUv(uvValue);
            String level = UVConverter.getSunburnAndVitamin(uvValue);
            sunDataDTO.setSunburn(level);
            sunDataDTO.setVitamin(level);
        } catch (Exception e){
            Log.i("DummyData", "uv could not be read");
        }
    }
}
