package com.ai.st.microservice.managers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StMicroserviceManagersApplication {

    public static void main(String[] args) {
        SpringApplication.run(StMicroserviceManagersApplication.class, args);
    }

}
