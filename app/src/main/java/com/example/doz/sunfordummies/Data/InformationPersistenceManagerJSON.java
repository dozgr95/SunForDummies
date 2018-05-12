package com.example.doz.sunfordummies.Data;

import android.util.Log;

import com.example.doz.sunfordummies.Business.Location.LocationDTO;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

public class InformationPersistenceManagerJSON implements InformationPersistenceManager {
    private File fileDir;

    public InformationPersistenceManagerJSON(File fileDir){
        this.fileDir = fileDir;
    }

    @Override
    public void saveSunInformation(DaySunInformation informationDTO) {
        File file = new File(fileDir, "app.json");

        try (Writer writer = new BufferedWriter(new FileWriter(file))){
            writer.append(informationDTO.toJSON());
        } catch (final IOException e){
            Log.e("Persistence", e.toString());
        }

        Log.e("Persistence", "Data saved");
    }

    @Override
    public DaySunInformation readSunInformation(Date day) {
        File file = new File(fileDir, "app.json");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String readLine;

            while((readLine = reader.readLine()) != null) {
                if(readLine.contains(DaySunInformation.formatter.format(new Date())))
                    return DaySunInformation.readJSON(readLine);
            }
        } catch (final IOException e){
            Log.e("Persistence", e.toString());
        }

        return new DaySunInformation();
    }
}