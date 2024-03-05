package edu.monash.group11.sunprotection.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reminder {

    private Integer id;
    private String email;
    private Long startTime;
    private Long endTime;
    private String lat;
    private String lon;
    @Nullable
    private Long notifyTime;
}
