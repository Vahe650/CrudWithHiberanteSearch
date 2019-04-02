package com.springBoot2;
//import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
//@EnableAdminServer
public class TaskEmployerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskEmployerApplication.class,args);
    }
}
