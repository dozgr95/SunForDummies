package ch.hslu.mobpro.sunfordummies.Business.SunData;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import ca.rmen.sunrisesunset.SunriseSunset;
import ch.hslu.mobpro.sunfordummies.Business.Api.EnergyDataAPI;
import ch.hslu.mobpro.sunfordummies.Business.Api.SunDataAPI;
import ch.hslu.mobpro.sunfordummies.Business.Api.UVIForecastAPI;
import ch.hslu.mobpro.sunfordummies.Business.Api.UvDataAPI;
import ch.hslu.mobpro.sunfordummies.Business.Location.LocationDTO;
import ch.hslu.mobpro.sunfordummies.Data.SunDataPersistenceManager;
import ch.hslu.mobpro.sunfordummies.Data.SunDataPersistenceManagerFactory;
import ch.hslu.mobpro.sunfordummies.Utils.EmptySunDataDTO;
import ch.hslu.mobpro.sunfordummies.Utils.SunDataDTO;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
public class SunDataService extends IntentService {
    private static final String ACTION_GETDATA = "ch.hslu.mobpro.sunfordummies.Business.SunData.action.GETDATA";

    private static final String EXTRA_RECEIVER = "ch.hslu.mobpro.sunfordummies.Business.SunData.extra.RECEIVER";
    private static final String EXTRA_LOCATION = "ch.hslu.mobpro.sunfordummies.Business.SunData.extra.LOCATION";
    private static final String EXTRA_DATE = "ch.hslu.mobpro.sunfordummies.Business.SunData.extra.DATE";

    private static final String subKEY = "68e0ca9dbc72500e3c5d8ea0c24d6306";
    private SunDataPersistenceManager persistenceManager;

    public SunDataService() {
        super("SunDataService");
    }

    /**
     * Starts this service to perform action getData with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void retrieveSunData(Context context, LocationDTO location, LocalDate date, SunDataResultReceiver receiver) {
        Intent intent = new Intent(context, SunDataService.class);
        intent.setAction(ACTION_GETDATA);
        intent.putExtra(EXTRA_LOCATION, location);
        intent.putExtra(EXTRA_DATE, date);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_RECEIVER);

            final String action = intent.getAction();
            if (ACTION_GETDATA.equals(action)) {
                final LocationDTO location = (LocationDTO)intent.getSerializableExtra(EXTRA_LOCATION);
                final LocalDate date = (LocalDate)intent.getSerializableExtra(EXTRA_DATE);

                handleActionGetData(resultReceiver, location, date);
            }
        }
    }

    private void handleActionGetData(ResultReceiver resultReceiver, LocationDTO location, LocalDate date) {
        persistenceManager = SunDataPersistenceManagerFactory.getPersistenceManager(this);
        SunDataDTO sunData = persistenceManager.findById(date, location.getCity());

        if(sunData instanceof EmptySunDataDTO){
            try {
                if(LocalDate.now().isEqual(date)) {
                    sunData = downloadSunData(location, date);
                    persistenceManager.saveSunInformation(sunData);
                } else {
                    List<SunDataDTO> sunDataDTOs = downloadForecastData(location);
                    for(SunDataDTO sunDataDTO : sunDataDTOs) {
                        List<LocalTime> times = getSunriseSunset(location);
                        sunDataDTO.setSunrise(times.get(0));
                        sunDataDTO.setSunset(times.get(1));

                        if(sunDataDTO.getDate().isEqual(date)) {
                            sunData = sunDataDTO;
                        }

                        persistenceManager.saveSunInformation(sunDataDTO);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        sendSunData(resultReceiver, sunData);
    }

    private SunDataDTO downloadSunData(LocationDTO location, LocalDate date) throws ExecutionException, InterruptedException {
        SunDataDTO sunData = new SunDataDTO();
        sunData.setDate(date);
        sunData.setCity(location.getCity());

        AsyncTask uvAsyncTask = new UvDataAPI(sunData).execute(createUvURL(location, date));
        AsyncTask sunAsyncTask = new SunDataAPI(sunData).execute(createSunURL(location, date));
        AsyncTask energyAsyncTask = new EnergyDataAPI(sunData).execute(createEnergyURL(location, date));
        sunAsyncTask.get();
        uvAsyncTask.get();
        energyAsyncTask.get();

        return sunData;
    }

    private String createUvURL(LocationDTO location, LocalDate date){
        String longitude = String.valueOf(location.getLongitude());
        String latitude = String.valueOf(location.getLatitude());

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("uvi")
                .appendQueryParameter("appid", subKEY)
                .appendQueryParameter("lat", latitude)
                .appendQueryParameter("lon", longitude);

        return builder.build().toString();
    }

    private String createEnergyURL(LocationDTO location, LocalDate date){
        String longitude;
        if(location.getLongitude() > 0){
            longitude = String.valueOf((int) location.getLongitude());
        }else{
            longitude = String.valueOf(45); // temp problem fix
        }
        String latitude;
        if(location.getLatitude() > 0){
             latitude = String.valueOf((int) location.getLatitude());
        }else{
             latitude = String.valueOf(45); // temp problem fix
        }

        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http")
                .authority("re.jrc.ec.europa.eu")
                .appendPath("pvgis5")
                .appendPath("PVcalc.php")
                .appendQueryParameter("lat", latitude)
                .appendQueryParameter("lon", longitude)
                .appendQueryParameter("peakpower", "1")
                .appendQueryParameter("loss", "15");
        return builder.build().toString();
    }

    private String createSunURL(LocationDTO location, LocalDate date){
        String longitude = String.valueOf(location.getLongitude());
        String latitude = String.valueOf(location.getLatitude());

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("weather")
                .appendQueryParameter("appid", subKEY)
                .appendQueryParameter("lat", latitude)
                .appendQueryParameter("lon", longitude);

        return builder.build().toString();
    }

    private List<SunDataDTO> downloadForecastData(LocationDTO locationDTO) throws ExecutionException, InterruptedException {
        List<SunDataDTO> sunDataDTOs = new ArrayList<>();
        try {
            UVIForecastAPI uviForecastAPI = new UVIForecastAPI(locationDTO.getCity());
            uviForecastAPI.execute(createUvForecastURL(locationDTO));
            sunDataDTOs.addAll(uviForecastAPI.get());
        } catch (MalformedURLException e) {
        }

        return sunDataDTOs;
    }

    private URL createUvForecastURL(LocationDTO location) throws MalformedURLException {
        String longitude = String.valueOf(location.getLongitude());
        String latitude = String.valueOf(location.getLatitude());

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("uvi")
                .appendPath("forecast")
                .appendQueryParameter("appid", subKEY)
                .appendQueryParameter("lat", latitude)
                .appendQueryParameter("lon", longitude)
                .appendQueryParameter("cnt", "5");

        return new URL(builder.build().toString());
    }

    private List<LocalTime> getSunriseSunset(LocationDTO location) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Calendar[] sunriseSunset = SunriseSunset.getSunriseSunset(calendar,
                location.getLatitude(), location.getLongitude());

        List<LocalTime> times = new ArrayList<>();
        times.add(convertInstant(sunriseSunset[0].toInstant()));
        times.add(convertInstant(sunriseSunset[1].toInstant()));

        return times;
    }

    private LocalTime convertInstant(Instant instant) {
        return LocalDateTime.ofInstant(instant,
                ZoneId.of("Europe/Paris")).toLocalTime();
    }

    private void sendSunData(ResultReceiver resultReceiver, SunDataDTO sunData){
        Bundle bundle = new Bundle();
        bundle.putSerializable(SunDataResultReceiver.PARAM_DATA, sunData);
        resultReceiver.send(SunDataResultReceiver.RESULT_CODE_OK, bundle);
    }
}