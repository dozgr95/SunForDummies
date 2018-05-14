package com.example.doz.sunfordummies.Data;

import android.util.Log;

import com.example.doz.sunfordummies.Utils.*;
import com.example.doz.sunfordummies.Business.Location.LocationDTO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

public class SunDataPersistenceManagerJSON implements SunDataPersistenceManager {
    private File fileDir;

    public SunDataPersistenceManagerJSON(File fileDir){
        this.fileDir = fileDir;
    }

    @Override
    public void saveSunInformation(SunDataDTO sunDataDTO) {
        File file = new File(fileDir, "app.json");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
            writer.newLine();
            writer.append(sunDataDTO.toJSON());
        } catch (final IOException e){
            Log.e("Persistence", e.toString());
        }

        Log.e("Persistence", "Data saved");
    }

    @Override
    public SunDataDTO findById(Date date, LocationDTO location) {
        File file = new File(fileDir, "app.json");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String readLine;

            while((readLine = reader.readLine()) != null) {
                if(checkIfIdMatches(readLine, date, location))
                    return SunDataDTO.readJSON(readLine);
            }
        } catch (final IOException e){
            Log.e("Persistence", e.toString());
        }

        return new EmptySunDataDTO();
    }

    private boolean checkIfIdMatches(String line, Date date, LocationDTO locationDTO){
        return line.contains(SunDataDTO.formatter.format(date)) &&
                line.contains(String.valueOf(locationDTO.getLatitude())) &&
                line.contains(String.valueOf(locationDTO.getLongitude()));
    }
}