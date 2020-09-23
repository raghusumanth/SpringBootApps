package com.limitservice.controller;

import com.limitservice.bean.LimitConfiguration;
import com.limitservice.config.Configuration;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsConfigurationController {

    @Autowired
    private Configuration configuration;

    @GetMapping("/limits")

    public LimitConfiguration retrieveLimitsFromConfigurations(){
        return new LimitConfiguration(configuration.getMaximum(),configuration.getMinimum());
    }
    @GetMapping("/fault-tolerance-example")
    @HystrixCommand(fallbackMethod = "fallbackRetrieveConfiguration")
    public LimitConfiguration retrieveConfiguration(){
        System.out.println(Math.random());
        if(Math.random()>0.5)
            throw new RuntimeException("Not Available");
        else
            return new LimitConfiguration(configuration.getMaximum(),configuration.getMinimum());

    }

    public LimitConfiguration fallbackRetrieveConfiguration(){
        return new LimitConfiguration(1,2);
    }
}
