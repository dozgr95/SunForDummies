package com.example.doz.sunfordummies.Business.Location;

public interface Locator {
    void addListener(LocationObserver observer);
    void removeListener(LocationObserver observer);
}
