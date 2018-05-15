package ch.hslu.mobpro.sunfordummies.Data;

import java.time.LocalDate;

import ch.hslu.mobpro.sunfordummies.Utils.SunDataDTO;

public interface SunDataPersistenceManager {
    void saveSunInformation(SunDataDTO sunDataDTO);
    SunDataDTO findById(LocalDate day, String city);
}
