package edu.monash.group11.sunprotection.controller;

import edu.monash.group11.sunprotection.intf.UVLevelService;
import edu.monash.group11.sunprotection.service.entity.ResponseDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/UVI")
@Slf4j
public class UVTrackController {

    @Resource
    private UVLevelService uvLevelService;


    @GetMapping("/UVIByCity/{city}")
    public ResponseDO getUVLevels(@PathVariable("city")String cityName){
        String[] location = uvLevelService.getLocation(cityName); //lat lon

        if(location == null || location.length == 0)
            return ResponseDO.fail("Invalid Australian city name!");
        log.info(location[0] + " " + location[1]);

        return ResponseDO.success(uvLevelService.getUVLevel(location[0], location[1]));
    }

    @GetMapping("/UVIByGeo/{lat}&{lon}")
    public ResponseDO getUVLevelByLatAndLon(@PathVariable("lat")String lat, @PathVariable("lon") String lon){
        String uvLevel = uvLevelService.getUVLevel(lat, lon);
        if(uvLevel == null)
            return ResponseDO.fail("Invalid geolocation");
        return ResponseDO.success(uvLevel);
    }
}