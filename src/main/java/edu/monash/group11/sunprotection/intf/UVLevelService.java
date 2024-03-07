package edu.monash.group11.sunprotection.intf;


public interface UVLevelService {
    String getUVLevel(String lat, String lon);

    Double getUVIByTime(String lat, String lon, long time);

    String[] getLocation(String cityName);
}
