package edu.monash.group11.sunprotection.service.enums;

import lombok.Data;

public enum ReminderStatusEnum {

    Upcoming(0, "Upcoming"), // Starts 48 hours later, now we can not get UVI - it will not be achieved in onBoarding
    Active(1, "Active"),
    Inactive(4, "Inactive");

    private int id;
    private String status;

    ReminderStatusEnum(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public static String getStatusById(int id){
        for(ReminderStatusEnum statusEnum: ReminderStatusEnum.values()){
            if(statusEnum.getId() == id)
                return statusEnum.getStatus();
        }
        return null;
    }
}
