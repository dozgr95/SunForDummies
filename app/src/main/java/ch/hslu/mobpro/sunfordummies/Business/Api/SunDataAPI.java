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

public class SunDataAPI extends AsyncTask<String, String, String> {
    private SunDataDTO sunDataDTO;
    public SunDataAPI(SunDataDTO sunDataDTO){
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
            //set sunDataDTO

        } catch (Exception e){
            Log.i("DummyData", "uv could not be read");
        }
    }
}