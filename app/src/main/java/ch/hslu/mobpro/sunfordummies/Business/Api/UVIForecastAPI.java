package ch.hslu.mobpro.sunfordummies.Business.Api;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ch.hslu.mobpro.sunfordummies.Utils.SunDataDTO;
import ch.hslu.mobpro.sunfordummies.Utils.UVConverter;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public class UVIForecastAPI extends AsyncTask<URL, String, List<SunDataDTO>> {
    @Override
    protected List<SunDataDTO> doInBackground(URL... urls) {
        URL request;
        JSONArray jsonArray = null;
        try {
            request = urls[0];
            HttpURLConnection httpConnection = (HttpURLConnection) request.openConnection();
            httpConnection.connect();
            if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream stream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String json = reader.readLine();
                jsonArray = new JSONArray(json);
            }

            return parseSunDataList(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<SunDataDTO> parseSunDataList(JSONArray jsonArray){
        List<SunDataDTO> sunDataDTOs = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObjectDay = jsonArray.getJSONObject(i);
                SunDataDTO sunDataDTO = new SunDataDTO();

                double uvIndex = Double.parseDouble(jsonObjectDay.optString("value"));
                sunDataDTO.setUv(uvIndex);
                String level = UVConverter.getSunburnAndVitamin(uvIndex);
                sunDataDTO.setSunburn(level);
                sunDataDTO.setVitamin(level);
                String date = jsonObjectDay.getString("date_iso");
                sunDataDTO.setDate(
                        LocalDateTime.parse(date.substring(0, date.length() - 1), ISO_DATE_TIME).
                                toLocalDate());
                sunDataDTOs.add(sunDataDTO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return sunDataDTOs;
    }
}
