package edu.monash.group11.sunprotection.controller;

import edu.monash.group11.sunprotection.intf.UVLevelService;
import edu.monash.group11.sunprotection.service.entity.ResponseDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/uv")
@Slf4j
public class UVTrackController {

    @Resource
    private UVLevelService uvLevelService;

    @GetMapping("/UVLevel/{city}")
    public ResponseDO getUVLevel(@PathVariable("city")String cityName){
        String[] location = uvLevelService.getLocation(cityName); //lat lon

        if(location == null || location.length == 0)
            return ResponseDO.fail("Invalid Australian city name!");
        log.info(location[0] + " " + location[1]);

        return ResponseDO.success(uvLevelService.getUVLevel(location[0], location[1]));
    }
}