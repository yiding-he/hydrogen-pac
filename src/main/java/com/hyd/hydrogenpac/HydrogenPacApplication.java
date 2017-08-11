package com.hyd.hydrogenpac;

import org.dizitart.no2.Nitrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HydrogenPacApplication {

    @Autowired
    NitriteWrapper nitriteWrapper;

    public static void main(String[] args) {
        SpringApplication.run(HydrogenPacApplication.class, args);
    }

    @Bean
    public Nitrite nitrite() {
        return nitriteWrapper.getNitrite();
    }
}
