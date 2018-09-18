package com.hyd.hydrogenpac;

import com.hyd.hydrogenpac.config.AuthConfig;
import com.hyd.hydrogenpac.config.CookieConfig;
import com.hyd.hydrogenpac.config.DbConfig;
import org.dizitart.no2.Nitrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({
        DbConfig.class, AuthConfig.class, CookieConfig.class
})
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
