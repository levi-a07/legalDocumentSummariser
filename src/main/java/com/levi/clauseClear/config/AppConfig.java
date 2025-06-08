package com.levi.clauseClear.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public Tika tika(){
        return new Tika();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
