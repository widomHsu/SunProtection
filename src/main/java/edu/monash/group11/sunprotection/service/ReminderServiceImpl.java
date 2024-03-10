package edu.monash.group11.sunprotection.service;

import com.google.gson.Gson;
import edu.monash.group11.sunprotection.intf.ReminderService;
import edu.monash.group11.sunprotection.intf.UVLevelService;
import edu.monash.group11.sunprotection.intf.mapper.ReminderMapper;
import edu.monash.group11.sunprotection.service.entity.Reminder;
import edu.monash.group11.sunprotection.service.entity.ResponseDO;
import edu.monash.group11.sunprotection.service.enums.ReminderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Implementation class for ReminderService responsible for creating reminders, retrieving reminder history,
 * and sending reminder messages.
 */
@Service
@Slf4j
public class ReminderServiceImpl implements ReminderService {
    @Resource
    ReminderMapper reminderMapper;
    @Resource
    UVLevelService uvLevelService;
    @Resource
    JavaMailSender mailSender;
    @Resource
    private Gson gson;
    @Value("${spring.mail.username}")
    private String from;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
//    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Creates a new reminder.
     * @param reminder The reminder to be created.
     * @return ResponseDO indicating success or failure of the reminder creation.
     */
    @Override
    public ResponseDO createReminder(Reminder reminder) {
        reminder.setEndTime(reminder.getStartTime() + 12 * 60 * 60);
        ResponseDO responseDO = checkReminder(reminder);
        if(responseDO.isSuccess())
            reminderMapper.insertReminder(reminder);
        return responseDO;
    }

    /**
     * Retrieves the history of reminders for a given email address.
     * Optionally sends an email with the reminder history.
     * @param email The email address associated with the reminders.
     * @param send Flag indicating whether to send an email with the reminder history (0: No, 1: Yes).
     * @return ResponseDO containing reminder history or indicating failure.
     */
    @Override
    public ResponseDO getHistoryOfReminders(String email, int send) {
        List<Reminder> reminders = reminderMapper.getRemindersByEmail(email);
        if(reminders == null || reminders.isEmpty())
            return ResponseDO.fail("You didn't create any reminders!");
//        if(send == 1){
//            executor.submit(() -> sendHistoryOfReminders(reminders, email));
//        }
        return ResponseDO.success(gson.toJson(reminders));
    }

    /**
     * Sends reminder messages based on scheduled tasks.
     */
    @Override
    @Scheduled(fixedDelay = 5000)
    public void sendEmail() {
        List<Reminder> reminders = reminderMapper.getRemindersToSend();
        for(Reminder reminder: reminders){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(reminder.getEmail());
            message.setSubject("Reminder - Sun Smart");
            message.setText("Don't forget to reapply sunscreen!");
            mailSender.send(message);
            log.info("Send an email to " + reminder.getEmail());

            reminder.setStatus(ReminderStatusEnum.Inactive.getId()); // only send email once
            reminderMapper.updateReminder(reminder);
        }
    }

    /**
     * Checks the UV index for a given reminder and updates the reminder accordingly.
     * @param reminder The reminder to check.
     */
    public ResponseDO checkReminder(Reminder reminder){
        Double UVI = uvLevelService.getCurrentUVI(reminder.getLat(), reminder.getLon());
        log.info("Current UVI in " + reminder.getLat() + ", " + reminder.getLon() + " is " + UVI);
        if(UVI == null)
            return ResponseDO.fail("Invalid geo-location");
        if(UVI > 3.0){
            reminder.setNextNotifyTime(reminder.getStartTime() + 2 * 60 * 60);// send an email in 2 hours
            if(reminder.getNextNotifyTime() >= reminder.getEndTime()){
                reminder.setStatus(ReminderStatusEnum.Inactive.getId());
                return new ResponseDO();
            }
            else{
                reminder.setStatus(ReminderStatusEnum.Active.getId());
                String notifyTime = sdf.format(new Date(reminder.getNextNotifyTime() * 1000));
                return new ResponseDO(true, "We will remind you at " + notifyTime);
            }
        }
        reminder.setStatus(ReminderStatusEnum.Inactive.getId());
        return ResponseDO.fail("The current UVI is " + UVI  + ", lower than 3.0, so you don't need to reapply");
    }

    /**
     * Sends an email with the history of reminders.
     * @param reminders The list of reminders.
     * @param email The recipient's email address.
     */
    public void sendHistoryOfReminders(List<Reminder> reminders, String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("All Reminders - Sun Smart");
        StringBuilder emailContent = new StringBuilder();
        int i = 1;
        for (Reminder reminder : reminders) {
            emailContent.append(i++).append(". From ")
                    .append(sdf.format(new Date(reminder.getStartTime() * 1000)))
                    .append(" to ")
                    .append(sdf.format(new Date(reminder.getEndTime() * 1000)))
                    .append(" - ")
                    .append(ReminderStatusEnum.getStatusById(reminder.getStatus()))
                    .append("\r\n");
        }
        message.setText(emailContent.toString());
        mailSender.send(message);
    }
}
