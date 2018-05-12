package com.example.doz.sunfordummies.Data;

import java.util.Date;

public interface InformationPersistenceManager {
    void saveSunInformation(DaySunInformation informationDTO);
    DaySunInformation readSunInformation(Date day);
}
