package edu.monash.group11.sunprotection.controller;

import edu.monash.group11.sunprotection.intf.UVLevelService;
import edu.monash.group11.sunprotection.service.entity.ResponseDO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

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
        String[] location = uvLevelService.getLocation(cityName); //lat lon

        if(location == null || location.length == 0)
            return ResponseDO.fail("Invalid Australian city name!");
        log.info(location[0] + " " + location[1]);

        return ResponseDO.success(uvLevelService.getUVLevel(location[0], location[1]));
    }

    /**
     * Retrieves UV level based on the provided latitude and longitude.
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return ResponseDO object containing the UV level information.
     */
    @GetMapping("/UVIByGeo/{lat}&{lon}")
    public ResponseDO getUVLevelByLatAndLon(@PathVariable("lat")String lat, @PathVariable("lon") String lon){
        String uvLevel = uvLevelService.getUVLevel(lat, lon);
        if(uvLevel == null)
            return ResponseDO.fail("Invalid geolocation");
        return ResponseDO.success(uvLevel);
    }
}