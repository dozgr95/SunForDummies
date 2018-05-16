package ch.hslu.mobpro.sunfordummies.Business.SunData;

import ch.hslu.mobpro.sunfordummies.Business.Api.SunDataAPI;
import ch.hslu.mobpro.sunfordummies.Business.Api.UvDataAPI;
import ch.hslu.mobpro.sunfordummies.Business.Location.LocationDTO;
import ch.hslu.mobpro.sunfordummies.Data.SunDataPersistenceManager;
import ch.hslu.mobpro.sunfordummies.Utils.EmptySunDataDTO;
import ch.hslu.mobpro.sunfordummies.Utils.SunDataDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class SunDataManager {
    private SunDataPersistenceManager persistenceManager;
    private String uvUrl =
            "http://api.openweathermap.org/data/2.5/uvi?appid=68e0ca9dbc72500e3c5d8ea0c24d6306";
    private String sunUrl = "https://www.sonnenverlauf.de/#/";


    public SunDataManager(SunDataPersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    }

    public SunDataDTO getSunData(LocalDate date, LocationDTO location){
        SunDataDTO data = persistenceManager.findById(date, location.getCity());

        if(data instanceof EmptySunDataDTO){
            data = new SunDataDTO();
            data.setDate(date);
            data.setCity(location.getCity());

            // Call UV API  //TODO: Datum Abhägingkeit
            uvUrl = this.uvUrl + "&lat="+ location.getLatitude() +"&lon=" + location.getLongitude();
            new UvDataAPI(data).execute(this.uvUrl);

            //TODO: Above X multiple calls
            //example https://www.sonnenverlauf.de/#/48.8583,2.2945,10/2018.05.16/12:00/0/0
            sunUrl = this.sunUrl + location.getLatitude() + "," + location.getLongitude() + ",10/";
            sunUrl = this.sunUrl + String.valueOf(date.getYear()) + "." +
                    String.valueOf(date.getMonthValue()) + "." + String.valueOf(date.getDayOfMonth()) +
                    "/12:00/0/0";

            new SunDataAPI(data).execute(this.sunUrl);

            data.setSunrise(LocalTime.now());
            data.setSunset(LocalTime.now());
            data.setMaxPosition(35);
            data.setAbove(5);
            data.setEnergy(8.9);
            data.setSunburn("medium");
            data.setVitamin("medium");

            persistenceManager.saveSunInformation(data);
        }

        return data;
    }
}
