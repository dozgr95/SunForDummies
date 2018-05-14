package com.example.doz.sunfordummies.Data;

import java.util.Date;
import com.example.doz.sunfordummies.Utils.SunDataDTO;
import com.example.doz.sunfordummies.Business.Location.LocationDTO;

public interface SunDataPersistenceManager {
    void saveSunInformation(SunDataDTO sunDataDTO);
    SunDataDTO findById(Date day, LocationDTO location);
}
