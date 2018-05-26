package ch.hslu.mobpro.sunfordummies.Business.Api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.hslu.mobpro.sunfordummies.Utils.SunDataDTO;

public class EnergyDataAPI extends AsyncTask<String, String, SunDataDTO> {
    private SunDataDTO sunDataDTO;
    public EnergyDataAPI(SunDataDTO sunDataDTO){
        this.sunDataDTO = sunDataDTO;
    }

    // e.g. http://re.jrc.ec.europa.eu/pvgis5/PVcalc.php?lat=45&lon=8&peakpower=1&loss=14

    @Override
    protected SunDataDTO doInBackground(String... strings) {
        URL request;
        //JSONObject jsonobject = null;
        try {
            request = new URL(strings[0]);
            Double kwh = 11.1;
            HttpURLConnection httpConnection = (HttpURLConnection) request.openConnection();
            httpConnection.connect();
            if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream stream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String tanga = "";
                for(int i = 0; i < 16; i++){
                    tanga = reader.readLine();
                }

                tanga = tanga.substring(3,7);
                kwh = Double.valueOf(tanga);
            }
            setSunDataDTO(kwh);
            return sunDataDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    protected void setSunDataDTO(double energy) {
        try{
            sunDataDTO.setEnergy(energy);
        } catch (Exception e){
            Log.i("DummyData", "energy data are not correctly read");
        }
    }
}