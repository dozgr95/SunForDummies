package ch.hslu.mobpro.sunfordummies.Utils;

public class UVConverter {
    public static String getSunburnAndVitamin(double uvIndex){
        String level = "not Available";
        if(uvIndex < 3){
            level = "low";
        } else if(uvIndex > 3  && uvIndex < 6.01){
            level = "medium";
        } else if (uvIndex > 6 && uvIndex < 10.01) {
            level = "high";
        } else if (uvIndex > 10.01) {
            level = "very high";
        }

        return level;
    }
}
