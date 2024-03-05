package edu.monash.group11.sunprotection.service;

import com.google.gson.Gson;
import edu.monash.group11.sunprotection.intf.UVLevelService;
import edu.monash.group11.sunprotection.service.entity.Weather;
import edu.monash.group11.sunprotection.service.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Service
public class UVLevelServiceImpl implements UVLevelService {

    @Resource
    private OkHttpClient client;
    @Resource
    private Gson gson;
    @Override
    public String getUVLevel(String lat, String lon) {
        // https://api.openweathermap.org/data/3.0/onecall?lat=33.44&lon=-94.04&exclude=minutely,daily&appid=b3c322f72dca971cf25f9a3af1502e75
        String url = "https://api.openweathermap.org/data/3.0/onecall" +
                "?lat=" + lat +
                "&lon=" + lon +
                "&exclude=minutely,daily" +
                "&appid=" + Constant.API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try(Response response = client.newCall(request).execute()){
            Weather weather = gson.fromJson(response.body().string(), Weather.class);
//            log.info(weather.toString());
            List<Weather.HourlyDTO> hourly = weather.getHourly();

            List<Double> list = new ArrayList<>();
            list.add(weather.getCurrent().getUvi());
            for(Weather.HourlyDTO hourlyDTO: hourly){
                list.add(hourlyDTO.getUvi());
            }

            return gson.toJson(list);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public String[] getLocation(String cityName) {
        // https://api.openweathermap.org/geo/1.0/direct?q=Melbourne,au&limit=5&appid=b3c322f72dca971cf25f9a3af1502e75
        String url = "https://api.openweathermap.org/geo/1.0/direct"
                + "?q=" + cityName
                + ",au"
                + "&appid=" + Constant.API_KEY;
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
