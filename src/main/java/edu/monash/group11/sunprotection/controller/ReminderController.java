package edu.monash.group11.sunprotection.controller;

import edu.monash.group11.sunprotection.controller.utils.RegexUtils;
import edu.monash.group11.sunprotection.intf.ReminderService;
import edu.monash.group11.sunprotection.service.entity.Reminder;
import edu.monash.group11.sunprotection.service.entity.ResponseDO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Controller class responsible for handling reminder-related requests.
 * Provides endpoints for creating reminders and retrieving reminder history.
 */
@RestController
@RequestMapping("/reminder")
@Slf4j
@Tag(name = "Reminder")
public class ReminderController {

    @Resource
    ReminderService reminderService;
    @Resource
    RegexUtils regexUtils;

    /**
     * Endpoint for creating a reminder.
     * @param reminder The reminder object to be created.
     * @return ResponseDO indicating success or failure of the reminder creation.
     */
    @PostMapping("/create")
    public ResponseDO create(@RequestBody Reminder reminder){
        if(!regexUtils.checkEmail(reminder.getEmail()))
            return ResponseDO.fail("Invalid Input");
        return reminderService.createReminder(reminder);
    }

    /**
     * Endpoint for retrieving reminder history.
     * @param email The email address associated with the reminders to retrieve.
     * @param send Flag indicating whether to send an email to the user (0: No, 1: Yes).
     * @return ResponseDO containing reminder history or indicating failure.
     */
    @GetMapping("/get/{email}&{send}")
    @Parameter(name = "send", description = "Whether to send an email to the user? 0: No 1: Yes.")
    @ApiResponse(responseCode = "200", description = "The response data is an array of Reminder Object")
    public ResponseDO getReminder(@PathVariable String email, @PathVariable int send){
        if(!regexUtils.checkEmail(email) || !(send == 0 || send == 1))
            return ResponseDO.fail("Invalid Input");
        return reminderService.getHistoryOfReminders(email, send);
    }
}
