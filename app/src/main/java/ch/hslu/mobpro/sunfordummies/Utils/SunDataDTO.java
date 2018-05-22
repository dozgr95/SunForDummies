package ch.hslu.mobpro.sunfordummies.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;


public class SunDataDTO implements Serializable {
    private String city;
    private LocalDate date;
    private double uv;
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

    public void setUv(double uv) {
        this.uv = uv;
    }

    public double getUv() {
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

    public String toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
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
    }

    public static SunDataDTO readJSON(String jsonString) throws JSONException {
        SunDataDTO sunDataDTO = new SunDataDTO();

        JSONObject jsonObject = new JSONObject(jsonString);
        sunDataDTO.setCity(jsonObject.getString("city"));
        sunDataDTO.setDate(LocalDate.parse(jsonObject.getString("day")));
        sunDataDTO.setUv(jsonObject.optDouble("uv", 0.0));
        sunDataDTO.setMaxPosition(jsonObject.optInt("maxPosition", 0));
        sunDataDTO.setSunburn(jsonObject.optString("sunburn", ""));
        sunDataDTO.setSunrise(parseLocalTime(jsonObject.optString("sunrise")));
        sunDataDTO.setSunset(parseLocalTime(jsonObject.optString("sunset")));
        sunDataDTO.setAbove(jsonObject.optInt("above", 0));
        sunDataDTO.setVitamin(jsonObject.optString("vitamin", ""));
        sunDataDTO.setEnergy(jsonObject.optDouble("energy", 0.0));

        return sunDataDTO;
    }

    private static LocalTime parseLocalTime(String time) {
        LocalTime localTime = null;
        if(time != null) {
            try {
                localTime = LocalTime.parse(time);
            } catch (DateTimeParseException ex) {
                ex.printStackTrace();
            }
        }

        return localTime;
    }
}
