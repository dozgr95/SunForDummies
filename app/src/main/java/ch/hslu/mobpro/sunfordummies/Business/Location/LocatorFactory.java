package ch.hslu.mobpro.sunfordummies.Business.Location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.security.ProviderException;
import java.util.Locale;

public class LocatorFactory {
    public static Locator createLocator(Context context) throws LocationPermissionException, ProviderException{
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new LocationPermissionException();
        }

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            throw new ProviderException();
        }

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        return new AndroidLocator(locationManager, geocoder);
    }
}
