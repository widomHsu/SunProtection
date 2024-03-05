package edu.monash.group11.sunprotection.service;

import edu.monash.group11.sunprotection.intf.ReminderService;
import edu.monash.group11.sunprotection.intf.UVLevelService;
import edu.monash.group11.sunprotection.intf.mapper.ReminderMapper;
import edu.monash.group11.sunprotection.service.entity.Reminder;
import edu.monash.group11.sunprotection.service.entity.ResponseDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class ReminderServiceImpl implements ReminderService {
    @Resource
    ReminderMapper reminderMapper;
    @Resource
    UVLevelService uvLevelService;
    @Resource
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public ResponseDO createReminder(Reminder reminder) {
        check(reminder);
        reminderMapper.insertReminder(reminder);
        return ResponseDO.success(null);
    }

    @Override
    @Scheduled(fixedDelay = 5000)
    public void sentEmail() {
        List<Reminder> reminders = reminderMapper.getReminders();
        for(Reminder reminder: reminders){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(reminder.getEmail());
            message.setSubject("Sun Smart Reminder");
            message.setText("Don't forget to reapply sunscreen!");
            mailSender.send(message);
            log.info("Send an email to " + reminder.getEmail());

            check(reminder);
            if(reminder.getNotifyTime() >= reminder.getEndTime()){
                reminderMapper.removeReminder(reminder.getId());
            }else{
                reminderMapper.updateReminder(reminder);
            }
        }
    }

    public void check(Reminder reminder){
        Double currentUVI = uvLevelService.getCurrentUVI(reminder.getLat(), reminder.getLon());
        int hours = 0;
        if(currentUVI > 3 && currentUVI < 6.0)
            hours = 3;
        else
            hours = 6;
        reminder.setNotifyTime(reminder.getStartTime() + hours * 60 * 60);
    }
}
