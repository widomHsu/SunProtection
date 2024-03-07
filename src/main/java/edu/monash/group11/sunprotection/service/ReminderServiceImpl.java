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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public ResponseDO createReminder(Reminder reminder) {
        reminder.setStartTime(reminder.getStartTime());
        reminder.setEndTime(reminder.getEndTime());
        checkReminder(reminder, reminder.getStartTime());
        reminderMapper.insertReminder(reminder);
        return ResponseDO.success(null);
    }

    @Override
    public ResponseDO getHistoryOfReminders(String email, int send) {
        List<Reminder> reminders = reminderMapper.getRemindersByEmail(email);
        if(reminders == null || reminders.isEmpty())
            return ResponseDO.fail("You didn't create any reminders!");
        if(send == 1){
            executor.submit(() -> sendHistoryOfReminders(reminders, email));
        }
        return ResponseDO.success(gson.toJson(reminders));
    }

    @Override
    @Scheduled(fixedDelay = 5000)
    public void sendReminderMessage() {
        List<Reminder> reminders = reminderMapper.getRemindersToSend();
        for(Reminder reminder: reminders){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(reminder.getEmail());
            message.setSubject("Reminder - Sun Smart");
            message.setText("Don't forget to reapply sunscreen!");
            mailSender.send(message);
            log.info("Send an email to " + reminder.getEmail());
            checkReminder(reminder, reminder.getNextNotifyTime());
            reminderMapper.updateReminder(reminder);
        }
    }

    public void checkReminder(Reminder reminder, long time){
        Double UVI = uvLevelService.getUVIByTime(reminder.getLat(), reminder.getLon(), time);
        log.info(sdf.format(new Date(time * 1000)) + " " + UVI);
        if(UVI == -1){
            // check upcoming reminder - it will not be achieved in onBoarding
            reminder.setStatus(ReminderStatusEnum.Upcoming.getId());
            return;
        }

        int hours = 0;
        if(UVI < 3.0)
            hours = 10;
        else if(UVI < 6.0)
            hours = 5;
        else if(UVI < 8.0)
            hours = 4;
        else if(UVI >= 9.0)
            hours = 3;

        reminder.setNextNotifyTime(reminder.getNextNotifyTime() == null ?
                reminder.getStartTime() + hours * 60 * 60 : reminder.getNextNotifyTime() + hours * 60 * 60);
        if(reminder.getNextNotifyTime() >= reminder.getEndTime())
            reminder.setStatus(ReminderStatusEnum.Inactive.getId());
        else
            reminder.setStatus(ReminderStatusEnum.Active.getId());

    }

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
