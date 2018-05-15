package ch.hslu.mobpro.sunfordummies.Business.SunData;

import ch.hslu.mobpro.sunfordummies.Business.Location.LocationDTO;
import ch.hslu.mobpro.sunfordummies.Business.UvData.UvDataAPI;
import ch.hslu.mobpro.sunfordummies.Data.SunDataPersistenceManager;
import ch.hslu.mobpro.sunfordummies.Utils.EmptySunDataDTO;
import ch.hslu.mobpro.sunfordummies.Utils.SunDataDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class SunDataManager {
    private SunDataPersistenceManager persistenceManager;
    private final String KEY = "68e0ca9dbc72500e3c5d8ea0c24d6306";
    private String url = "http://api.openweathermap.org/data/2.5/uvi?appid=";

    public SunDataManager(SunDataPersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    }

    public SunDataDTO getSunData(LocalDate date, LocationDTO location){
        SunDataDTO data = persistenceManager.findById(date, location.getCity());

        if(data instanceof EmptySunDataDTO){
            data = new SunDataDTO();
            data.setDate(date);
            data.setCity(location.getCity());
            data.setSunrise(LocalTime.now());
            data.setSunset(LocalTime.now());
            data.setMaxPosition(35);
            data.setAbove(5);
            data.setEnergy(8.9);
            data.setSunburn("medium");
            data.setVitamin("medium");

            //TODO: Datum Abhägingkeit
            //this.url = this.url + KEY;
            //url = this.url + "&lat="+ location.getLatitude() +"&lon=" + location.getLongitude();
            //new UvDataAPI(data).execute(this.url);
            //API call
            persistenceManager.saveSunInformation(data);
        }

        return data;
    }
}
