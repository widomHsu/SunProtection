package edu.monash.group11.sunprotection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SunProtectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SunProtectionApplication.class, args);
    }

}
