package edu.monash.group11.sunprotection.controller;

import com.google.gson.Gson;
import edu.monash.group11.sunprotection.intf.ReminderService;
import edu.monash.group11.sunprotection.intf.UVLevelService;
import edu.monash.group11.sunprotection.service.entity.Reminder;
import edu.monash.group11.sunprotection.service.entity.ResponseDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/reminder")
@Slf4j
public class ReminderController {

    @Resource
    ReminderService reminderService;

    @PostMapping("/create")
    public ResponseDO create(@RequestBody Reminder reminder){

        return reminderService.createReminder(reminder);
    }


}
