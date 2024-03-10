package edu.monash.group11.sunprotection.controller;

import edu.monash.group11.sunprotection.intf.UVLevelService;
import edu.monash.group11.sunprotection.service.entity.ResponseDO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;

/**
 * Controller class responsible for handling requests related to UV (Ultraviolet) index tracking.
 * This controller provides endpoints to retrieve UV levels based on city names or geographical coordinates.
 */
@RestController
@RequestMapping("/UVI")
@Slf4j
@Tag(name = "UVI")
public class UVTrackController {

    @Resource
    private UVLevelService uvLevelService;

    /**
     * Retrieves UV levels based on the provided city name.
     * @param cityName The name of the city for which UV levels are to be retrieved.
     * @return ResponseDO object containing the UV level information.
     */
    @GetMapping("/UVIByCity/{city}")
    public ResponseDO getUVLevels(@PathVariable("city")String cityName){
        String[] geo = uvLevelService.getLocation(cityName); //lat lon

        if(geo == null || geo.length == 0)
            return ResponseDO.fail("Invalid Australian city name!");
        log.info(geo[0] + " " + geo[1]);

        return ResponseDO.success(uvLevelService.getUVIHourly(geo[0], geo[1]));
    }

    /**
     * Retrieves UV level based on the provided latitude and longitude.
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return ResponseDO object containing the UV level information.
     */
    @GetMapping("/UVIByGeo/{lat}&{lon}")
    public ResponseDO getUVLevelByLatAndLon(@PathVariable("lat")String lat, @PathVariable("lon") String lon){
        String UVIs = uvLevelService.getUVIHourly(lat, lon);
        if(UVIs == null)
            return ResponseDO.fail("Invalid geolocation");
        return ResponseDO.success(UVIs);
    }

    @GetMapping(value = "/impacts",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImpacts() throws Exception {
        File file = new File("src/main/resources/static/img.png");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }
}