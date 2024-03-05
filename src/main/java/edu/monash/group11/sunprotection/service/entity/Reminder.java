package edu.monash.group11.sunprotection.service.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Reminder {

    private String email;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String cityName;

}
