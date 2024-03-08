package edu.monash.group11.sunprotection.service;

import com.google.gson.Gson;
import edu.monash.group11.sunprotection.intf.UVLevelService;
import edu.monash.group11.sunprotection.service.entity.Weather;
import edu.monash.group11.sunprotection.service.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the UVLevelService interface providing methods for retrieving UV levels and location information.
 */
@Slf4j
@Service
public class UVLevelServiceImpl implements UVLevelService {

    @Resource
    private OkHttpClient client;
    @Resource
    private Gson gson;
    @Value("${API.key}")
    private String API_KEY;

    /**
     * Retrieves UV levels for a given latitude and longitude.
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return JSON string representing the UV levels.
     */
    @Override
    public String getUVLevel(String lat, String lon) {
        // https://api.openweathermap.org/data/3.0/onecall?lat=33.44&lon=-94.04&exclude=minutely,daily&appid=b3c322f72dca971cf25f9a3af1502e75
        String url = "https://api.openweathermap.org/data/3.0/onecall" +
                "?lat=" + lat +
                "&lon=" + lon +
                "&exclude=minutely,daily" +
                "&appid=" + API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try(Response response = client.newCall(request).execute()){
            Weather weather = gson.fromJson(response.body().string(), Weather.class);
            List<Weather.HourlyDTO> hourly = weather.getHourly();

            LinkedHashMap<String, String> map = new LinkedHashMap<>();

            for(Weather.HourlyDTO hourlyDTO: hourly){
                map.put(hourlyDTO.getDt().toString(), hourlyDTO.getUvi().toString());
            }

            return gson.toJson(map);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves the UV index for a given latitude, longitude, and time.
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @param time The time for which UV index is to be retrieved (in seconds).
     * @return The UV index at the specified time.
     */
    @Override
    public Double getUVIByTime(String lat, String lon, long time) {
        String url = "https://api.openweathermap.org/data/3.0/onecall" +
                "?lat=" + lat +
                "&lon=" + lon +
                "&exclude=minutely,daily" +
                "&appid=" + API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try(Response response = client.newCall(request).execute()){
            Weather weather = gson.fromJson(response.body().string(), Weather.class);
            List<Weather.HourlyDTO> hourly = weather.getHourly();
            int size = hourly.size();
            if(time >= hourly.get(size-1).getDt() + 60 * 60){
                return (double) -1;
            }
            for(int i = 0; i < size-1; i++){
                if(time >= hourly.get(i).getDt() && time <= hourly.get(i+1).getDt())
                    return hourly.get(i).getUvi();
            }
            return hourly.get(size-1).getUvi();
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves latitude and longitude for a given city name.
     * @param cityName The name of the city.
     * @return An array containing latitude and longitude.
     */
    @Override
    public String[] getLocation(String cityName) {
        // https://api.openweathermap.org/geo/1.0/direct?q=Melbourne,au&limit=5&appid=b3c322f72dca971cf25f9a3af1502e75
        String url = "https://api.openweathermap.org/geo/1.0/direct"
                + "?q=" + cityName
                + ",au"
                + "&appid=" + API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try(Response response = client.newCall(request).execute()){
            String result = response.body().string();
            Pattern pattern = Pattern.compile(Constant.REGEX_FIND_LOCATION);
            Matcher matcher = pattern.matcher(result);
            String[] res = null;
            while (matcher.find()) {
                res = new String[]{matcher.group(1), matcher.group(2)};
            }
            return res;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }
}
