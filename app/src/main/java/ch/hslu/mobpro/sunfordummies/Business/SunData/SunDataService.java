package ch.hslu.mobpro.sunfordummies.Business.SunData;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

import ch.hslu.mobpro.sunfordummies.Business.Api.SunDataAPI;
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

    private static final String uvKEY = "68e0ca9dbc72500e3c5d8ea0c24d6306";

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
        SunDataPersistenceManager persistenceManager = SunDataPersistenceManagerFactory.getPersistenceManager(this);
        SunDataDTO sunData = persistenceManager.findById(date, location.getCity());

        if(sunData instanceof EmptySunDataDTO){
            sunData = new SunDataDTO();
            sunData.setDate(date);
            sunData.setCity(location.getCity());

            try {
                new UvDataAPI(sunData).execute(createUvURL(location, date)).get();
                new SunDataAPI(sunData).execute(createSunURL(location, date)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            persistenceManager.saveSunInformation(sunData);
        }

        sendSunData(resultReceiver, sunData);
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
                .appendQueryParameter("appid", uvKEY)
                .appendQueryParameter("lat", latitude)
                .appendQueryParameter("lon", longitude);

        return builder.build().toString();
    }

    private String createSunURL(LocationDTO location, LocalDate date){
        String longitude = String.valueOf(location.getLongitude());
        String latitude = String.valueOf(location.getLatitude());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateString = date.format(formatter);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.sonnenverlauf.de")
                .appendPath("#")
                .appendPath(longitude + "," + latitude + ",10")
                .appendPath(dateString)
                .appendPath("0")
                .appendPath("0");
        //example https://www.sonnenverlauf.de/#/48.8583,2.2945,10/2018.05.16/12:00/0/0
        return builder.build().toString();
    }

    private void sendSunData(ResultReceiver resultReceiver, SunDataDTO sunData){
        Bundle bundle = new Bundle();
        bundle.putSerializable(SunDataResultReceiver.PARAM_DATA, sunData);
        resultReceiver.send(SunDataResultReceiver.RESULT_CODE_OK, bundle);
    }
}