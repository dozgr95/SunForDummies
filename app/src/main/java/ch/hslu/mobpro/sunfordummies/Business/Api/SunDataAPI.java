package ch.hslu.mobpro.sunfordummies.Business.Api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ch.hslu.mobpro.sunfordummies.Utils.SunDataDTO;

public class SunDataAPI extends AsyncTask<String, String, SunDataDTO> {
    private SunDataDTO sunDataDTO;
    public SunDataAPI(SunDataDTO sunDataDTO){
        this.sunDataDTO = sunDataDTO;
    }

    @Override
    protected SunDataDTO doInBackground(String... strings) {
        URL request;
        JSONObject jsonobject = null;
        try {
            request = new URL(strings[0]);
            HttpURLConnection httpConnection = (HttpURLConnection) request.openConnection();
            httpConnection.connect();
            if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream stream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String json = reader.readLine();
                jsonobject = new JSONObject(json);
            }

            setSunDataDTO(jsonobject);
            return sunDataDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void setSunDataDTO(JSONObject values) {
        try{
            //set sunDataDTO
            JSONObject sys = values.getJSONObject("sys");
            long sunriseStamp = Long.valueOf(sys.optString("sunrise"))*1000;
            long sunsetStamp = Long.valueOf(sys.optString("sunset"))*1000;
            TimeZone timeZone = TimeZone.getTimeZone("Europe/Zurich");
            Date sunriseDate = new java.util.Date(sunriseStamp + timeZone.getOffset(sunriseStamp));
            Date sunsetDate = new java.util.Date(sunsetStamp  + timeZone.getOffset(sunsetStamp));
            Format formatter = new SimpleDateFormat("HH:mm");
            sunDataDTO.setSunrise(LocalTime.parse(formatter.format(sunriseDate)));
            sunDataDTO.setSunset(LocalTime.parse(formatter.format(sunsetDate)));
        } catch (Exception e){
            Log.i("DummyData", "sun data is not correctly read");
        }
    }
}