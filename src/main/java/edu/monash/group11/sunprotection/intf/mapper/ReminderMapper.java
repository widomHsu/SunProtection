package edu.monash.group11.sunprotection.intf.mapper;

import edu.monash.group11.sunprotection.service.entity.Reminder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReminderMapper {
    @Insert("insert into reminder(email, start_time, end_time, lat, lon, next_notify_time, status) values " +
            "(#{email}, #{startTime}, #{endTime}, #{lat}, #{lon}, #{nextNotifyTime}, #{status})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insertReminder(Reminder reminder);

    @Select("select * from reminder where status = 1 and next_notify_time <= UNIX_TIMESTAMP()")
    List<Reminder> getRemindersToSend();

    @Update("update reminder set next_notify_time = #{nextNotifyTime}, status = #{status} where id = #{id}")
    int updateReminder(Reminder reminder);

    @Select("select * from reminder where email = #{email}")
    List<Reminder> getRemindersByEmail(String email);
}
