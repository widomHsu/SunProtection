package edu.monash.group11.sunprotection.intf;


public interface UVLevelService {
    String getUVLevel(String lat, String lon);

    String[] getLocation(String cityName);
}
