package ch.hslu.mobpro.sunfordummies;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ch.hslu.mobpro.sunfordummies.Business.Location.LocationDTO;
import ch.hslu.mobpro.sunfordummies.Business.Location.LocationObserver;
import ch.hslu.mobpro.sunfordummies.Business.Location.LocationPermissionException;
import ch.hslu.mobpro.sunfordummies.Business.Location.Locator;
import ch.hslu.mobpro.sunfordummies.Business.Location.LocatorFactory;
import ch.hslu.mobpro.sunfordummies.Business.SunData.SunDataManager;
import ch.hslu.mobpro.sunfordummies.Business.SunData.SunDataManagerFactory;
import ch.hslu.mobpro.sunfordummies.Utils.SunDataDTO;

import java.security.ProviderException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DayActivity extends AppCompatActivity implements LocationObserver {
    private static final int LOCATION_PERMISSION_REQUEST = 24;
    private Locator locator;
    private SunDataManager sunDataManager;
    private LocationDTO currentLocation;
    private SunDataDTO currentSunData;
    private LocalDate currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);

        sunDataManager = SunDataManagerFactory.getSunDataManager(this);

        try {
            registerLocationObserver();
        } catch (LocationPermissionException e) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, LOCATION_PERMISSION_REQUEST);
        } catch (ProviderException e){
            //inform user
        }

        // test
        currentDate = LocalDate.now();
        sunDataManager.getSunData(currentDate, currentLocation);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try {
                        registerLocationObserver();
                    } catch (LocationPermissionException e) {
                        Log.e("sunfordummies", "Permission not received.");
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void registerLocationObserver() throws LocationPermissionException, ProviderException {
        locator = LocatorFactory.createLocator(this);
        locator.addListener(this);
    }

    @Override
    public void update(final LocationDTO location) {
        LocalDate chooseDate = LocalDate.now();
        if(currentSunData != null) {
            chooseDate = currentSunData.getDate();
        }
        currentLocation = location;
        currentSunData = sunDataManager.getSunData(chooseDate, location);

        runOnUiThread(new Runnable() {
            public void run() {
                updateSunDataOnGUI();
            }
        });
    }

    private LocalDate getDayBefore(LocalDate date){
        return date.minusDays(1);
    }

    @Override
    protected void onDestroy() {
        locator.removeListener(this);
        super.onDestroy();
    }

    public void onClickPrevious(View button) {
        LocalDate currentDate = currentSunData.getDate();
        currentSunData = sunDataManager.getSunData(getDayBefore(currentDate), currentLocation);
        updateSunDataOnGUI();
    }

    public void showInfoVitamin(View button){
        this.showMessage(getResources().getString(R.string.infoVitamin), getResources().getString(R.string.vitamin));
    }

    public void showInfoAbove(View button){
        this.showMessage(getResources().getString(R.string.infoAbove), getResources().getString(R.string.above));
    }

    public void showInfoUv(View button){
        this.showMessage(getResources().getString(R.string.infoUv), getResources().getString(R.string.uv));
    }

    public void showInfoSunburn(View button){
        this.showMessage(getResources().getString(R.string.infoSunburn), getResources().getString(R.string.sunburn));
    }

    public void showInfoEnergy(View button){
        this.showMessage(getResources().getString(R.string.infoEnergy), getResources().getString(R.string.energy));
    }

    private void showMessage(String message, String title){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.setCancelable(true);
        dialog.create().show();
    }

    private void updateSunDataOnGUI(){
        TextView txtDateCity = findViewById(R.id.txtDateCity);
        TextView txtMaxPosition = findViewById(R.id.txtMaxPosition);
        TextView txtSunburn = findViewById(R.id.txtSunburn);
        TextView txtSunrise = findViewById(R.id.txtSunrise);
        TextView txtSunset = findViewById(R.id.txtSunset);
        TextView txtUV = findViewById(R.id.txtUV);
        TextView txtAbove35 = findViewById(R.id.txtAbove);
        TextView txtVitamin = findViewById(R.id.txtVitamin);
        TextView txtEnergy = findViewById(R.id.txtEnergy);

        txtDateCity.setText(getDateString(currentSunData.getDate()) + "  " + currentSunData.getCity());
        txtMaxPosition.setText(String.valueOf(currentSunData.getMaxPosition()));
        txtSunburn.setText(currentSunData.getSunburn());
        txtSunrise.setText(getTimeString(currentSunData.getSunrise()));
        txtSunset.setText(getTimeString(currentSunData.getSunset()));
        txtAbove35.setText(String.valueOf(currentSunData.getAbove()));
        txtVitamin.setText(currentSunData.getVitamin());
        txtEnergy.setText(String.valueOf(currentSunData.getEnergy()));
        txtUV.setText(String.valueOf(currentSunData.getUv()));
    }

    private String getDateString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    private String getTimeString(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }
}
