package com.infytelconfiguiration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;


@SpringBootApplication
@EnableConfigServer
public class InfytelConfiguirationApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfytelConfiguirationApplication.class, args);
    }

}
