package edu.monash.group11.sunprotection.service.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "nextNotifyTime and status can be excluded from the HTTP request")
public class Reminder {

    private Integer id;
    private String email;
    @Schema(description = "Unix time (second)")
    private Long startTime;
    @Schema(description = "Unix time (second)")
    private Long endTime;
    private String lat;
    private String lon;
    @Nullable
    private Long nextNotifyTime;
    @Nullable
    @Schema(description = "0 for upcoming, 1 for active, 4 for inactive - \"upcoming\" is just a redundant status, it doesn't work in onBoarding project")
    private Integer status; // see ReminderStatusEnum
}
