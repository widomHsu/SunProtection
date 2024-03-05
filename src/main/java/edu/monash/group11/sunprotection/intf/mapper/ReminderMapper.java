package edu.monash.group11.sunprotection.intf.mapper;

import edu.monash.group11.sunprotection.service.entity.Reminder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReminderMapper {
    @Insert("insert into reminder(email, start_time, end_time, lat, lon, notify_time) values " +
            "(#{email}, #{startTime}, #{endTime}, #{lat}, #{lon}, #{notifyTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insertReminder(Reminder reminder);

    @Select("select * from reminder where " +
            "notify_time <= UNIX_TIMESTAMP()")
    List<Reminder> getReminders();
    @Delete("delete from reminder where id = #{id}")
    int removeReminder(int id);

    @Update("update reminder set notify_time = #{notifyTime}")
    int updateReminder(Reminder reminder);
}
