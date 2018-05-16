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

public class UvDataAPI extends AsyncTask<String, String, Double> {
    private SunDataDTO sunDataDTO;

    public UvDataAPI(SunDataDTO sunDataDTO){
        this.sunDataDTO = sunDataDTO;
    }

    @Override
    protected Double doInBackground(String... strings) {
        URL request;
        String uvValue = "";
        try {
            request = new URL(strings[0]);
            HttpURLConnection httpConnection = (HttpURLConnection) request.openConnection();
            httpConnection.connect();
            if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream stream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String json = reader.readLine();
                JSONObject jsonobject = new JSONObject(json);
                uvValue = jsonobject.optString("value");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Double.valueOf(uvValue);
    }

    protected void onPostExecute(Double uvValue) {
        try{
            sunDataDTO.setUv(uvValue);
            if(uvValue < 3){
                sunDataDTO.setSunburn("low");
                sunDataDTO.setVitamin("low");
            } else if(uvValue > 3  && uvValue < 6){
                sunDataDTO.setSunburn("medium");
                sunDataDTO.setVitamin("medium");
            } else if (uvValue > 6 && uvValue < 9) {
                sunDataDTO.setSunburn("high");
                sunDataDTO.setVitamin("high");
            } else if (uvValue > 10) {
                sunDataDTO.setSunburn("very high");
                sunDataDTO.setVitamin("very high");
            } else {
                sunDataDTO.setSunburn("not Available");
                sunDataDTO.setVitamin("not Available");
            }
        } catch (Exception e){
            Log.i("DummyData", "uv could not be read");
        }
    }
}
