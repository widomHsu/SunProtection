package edu.monash.group11.sunprotection.intf;


public interface UVLevelService {
    String getUVIHourly(String lat, String lon);

    Double getCurrentUVI(String lat, String lon);

    String[] getLocation(String cityName);
}
