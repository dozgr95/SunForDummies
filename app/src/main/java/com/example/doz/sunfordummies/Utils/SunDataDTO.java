package com.example.doz.sunfordummies.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;


public class SunDataDTO {
    private String city;
    private LocalDate date;
    private int uv;
    private int maxPosition;
    private LocalTime sunrise;
    private LocalTime sunset;
    private String sunburn;
    private int above;
    private String vitamin;
    private double energy;

    public SunDataDTO(){
    }

    public LocalDate getDate() {
        return LocalDate.parse(date.toString()) ;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public int getUv() {
        return uv;
    }

    public int getMaxPosition() {
        return maxPosition;
    }

    public void setMaxPosition(int maxPosition) {
        this.maxPosition = maxPosition;
    }

    public LocalTime getSunrise() {
        return sunrise;
    }

    public void setSunrise(LocalTime sunrise) {
        this.sunrise = sunrise;
    }

    public LocalTime getSunset() {
        return sunset;
    }

    public void setSunset(LocalTime sunset) {
        this.sunset = sunset;
    }

    public String getSunburn() {
        return sunburn;
    }

    public void setSunburn(String sunburn) {
        this.sunburn = sunburn;
    }

    public int getAbove() {
        return above;
    }

    public void setAbove(int above) {
        this.above = above;
    }

    public String getVitamin() {
        return vitamin;
    }

    public void setVitamin(String vitamin) {
        this.vitamin = vitamin;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public String toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("city", getCity());
            jsonObject.put("day", getDate());
            jsonObject.put("uv", getUv());
            jsonObject.put("maxPosition", getMaxPosition());
            jsonObject.put("sunburn", getSunburn());
            jsonObject.put("sunrise", getSunrise());
            jsonObject.put("sunset", getSunset());
            jsonObject.put("above", getAbove());
            jsonObject.put("vitamin", getVitamin());
            jsonObject.put("energy", getEnergy());
            return jsonObject.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    public static SunDataDTO readJSON(String jsonString){
        SunDataDTO sunDataDTO = new SunDataDTO();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            sunDataDTO.setCity(jsonObject.getString("city"));
            sunDataDTO.setDate(LocalDate.parse(jsonObject.getString("day")));
            sunDataDTO.setUv(jsonObject.getInt("uv"));
            sunDataDTO.setMaxPosition(jsonObject.getInt("maxPosition"));
            sunDataDTO.setSunburn(jsonObject.getString("sunburn"));
            sunDataDTO.setSunrise(LocalTime.parse(jsonObject.getString("sunrise")));
            sunDataDTO.setSunset(LocalTime.parse(jsonObject.getString("sunset")));
            sunDataDTO.setAbove(jsonObject.getInt("above"));
            sunDataDTO.setVitamin(jsonObject.getString("vitamin"));
            sunDataDTO.setEnergy(jsonObject.getDouble("energy"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sunDataDTO;
    }
}
