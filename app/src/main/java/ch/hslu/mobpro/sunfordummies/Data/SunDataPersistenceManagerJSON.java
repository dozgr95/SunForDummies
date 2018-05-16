package ch.hslu.mobpro.sunfordummies.Data;

import android.util.Log;

import ch.hslu.mobpro.sunfordummies.Utils.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

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
    public SunDataDTO findById(LocalDate date, String city) {
        File file = new File(fileDir, "app.json");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String readLine;

            while((readLine = reader.readLine()) != null) {
                if(checkIfIdMatches(readLine, date, city))
                    return SunDataDTO.readJSON(readLine);
            }
        } catch (final IOException e){
            Log.e("Persistence", e.toString());
        }

        return new EmptySunDataDTO();
    }

    private boolean checkIfIdMatches(String line, LocalDate date, String city){
        return line.contains(date.toString()) &&
                line.contains(city);
    }
}