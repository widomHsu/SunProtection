package edu.monash.group11.sunprotection.intf;

import edu.monash.group11.sunprotection.service.entity.Reminder;
import edu.monash.group11.sunprotection.service.entity.ResponseDO;

public interface ReminderService {
    ResponseDO createReminder(Reminder reminder);

    void sentEmail();
}
