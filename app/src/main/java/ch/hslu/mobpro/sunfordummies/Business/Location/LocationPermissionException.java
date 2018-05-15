package ch.hslu.mobpro.sunfordummies.Business.Location;

public class LocationPermissionException extends Exception {
    public LocationPermissionException(){
        super();
    }

    public LocationPermissionException(String message){
        super(message);
    }
}
