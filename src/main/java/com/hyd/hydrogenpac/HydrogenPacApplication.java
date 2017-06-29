package com.hyd.hydrogenpac;

import com.hyd.hydrogenpac.redis.Redis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@SpringBootApplication
public class HydrogenPacApplication {

    public static void main(String[] args) {
        SpringApplication.run(HydrogenPacApplication.class, args);
    }

    @Bean
    public Redis redis(RedisConnectionFactory factory) {

        if (factory instanceof JedisConnectionFactory) {
            String host = ((JedisConnectionFactory) factory).getHostName();
            int port = ((JedisConnectionFactory) factory).getPort();
            return new Redis(host, port, 5);
        } else {
            throw new IllegalArgumentException(factory.getClass() + " not supported yet.");
        }
    }

}
