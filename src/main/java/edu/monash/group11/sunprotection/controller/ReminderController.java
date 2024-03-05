package edu.monash.group11.sunprotection.controller;

import com.google.gson.Gson;
import edu.monash.group11.sunprotection.intf.UVLevelService;
import edu.monash.group11.sunprotection.service.entity.Reminder;
import edu.monash.group11.sunprotection.service.entity.ResponseDO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/reminder")
public class ReminderController {

    @Resource
    UVTrackController uvTrackController;
    @Resource
    Gson gson;
    @PostMapping("/create")
    public ResponseDO create(@RequestBody Reminder reminder){
        String cityName = reminder.getCityName();
        ResponseDO responseDO = uvTrackController.getUVLevel(cityName);
        if(!responseDO.isSuccess())
            return ResponseDO.fail(responseDO.getMsg());
        Double[] doubles = gson.fromJson((String) responseDO.getData(), Double[].class);
        Double UVLevel = doubles[0];



        return ResponseDO.success(null);
    }
}
