package ch.hslu.mobpro.sunfordummies.Business.Api;

import android.os.AsyncTask;
import android.util.Log;

import ch.hslu.mobpro.sunfordummies.Utils.SunDataDTO;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UvDataAPI extends AsyncTask<String, String, String> {
    private SunDataDTO sunDataDTO;
    public UvDataAPI(SunDataDTO sunDataDTO){
        this.sunDataDTO = sunDataDTO;
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
            sunDataDTO.setUv(uv);
            if(uv < 3){
                sunDataDTO.setSunburn("low");
                sunDataDTO.setVitamin("low");
            } else if(uv > 3  && uv < 6){
                sunDataDTO.setSunburn("medium");
                sunDataDTO.setVitamin("medium");
            } else if (uv > 6 && uv < 9) {
                sunDataDTO.setSunburn("high");
                sunDataDTO.setVitamin("high");
            } else if (uv > 10) {
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
