package com.example.doz.sunfordummies.Utils;

import com.example.doz.sunfordummies.Business.Location.LocationDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SunDataDTO {
    private LocationDTO location;
    private Date date;
    private String uv;
    public static DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    public SunDataDTO(){
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public Date getDate() {
        return new Date(date.getTime()) ;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", getLocation().getLatitude());
            jsonObject.put("longitude", getLocation().getLongitude());
            jsonObject.put("day", formatter.format(getDate()));
            jsonObject.put("uv", getUv());

            return jsonObject.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    public static SunDataDTO readJSON(String jsonString){
        SunDataDTO information = new SunDataDTO();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            LocationDTO location = new LocationDTO();
            location.setLatitude(jsonObject.getDouble(("latitude")));
            location.setLongitude(jsonObject.getDouble("longitude"));
            information.setLocation(location);
            information.setDate(formatter.parse(jsonObject.getString("day")));
            information.setUv(jsonObject.getString("uv"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return information;
    }
}
