package com.appsdeveloperblog.photoapp.api.users;

import com.appsdeveloperblog.photoapp.api.users.shared.FeignErrorDecoder;
import feign.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

/*@SpringBootApplication(
        exclude = {
            SecurityAutoConfiguration.class,
            ManagementWebSecurityAutoConfiguration.class
        })*/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class PhotoAppApiApplication {

    @Autowired
    Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(PhotoAppApiApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringApplicationContext springApplicationContext() {
        return new SpringApplicationContext();
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    
    @Bean
    @Profile("default")
    public String createDefaultBean() {
        System.out.println("Printing from Default Bean");
        System.out.println("environment = " + environment.getProperty("temp.profile.name"));
        System.out.println("environment = " + environment.getProperty("spring.profiles.active"));
        return "Development bean";
    }
    
    
    @Bean
    @Profile("!production")
    public String createDevelopmentBean() {
        System.out.println("Printing from Development Bean");
        System.out.println("environment = " + environment.getProperty("temp.profile.name"));
        System.out.println("environment = " + environment.getProperty("spring.profiles.active"));
        return "Development bean";
    }

    @Bean
    @Profile("production")
    public String createProductionBean() {
        System.out.println("Printing from Production Bean");
        System.out.println("environment = " + environment.getProperty("temp.profile.name"));
        return "Production bean";
    }

    /*
    @Bean
    public FeignErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }*/
}
