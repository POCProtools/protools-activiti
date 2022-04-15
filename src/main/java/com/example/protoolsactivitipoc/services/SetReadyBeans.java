package com.example.protoolsactivitipoc.services;

import org.activiti.api.process.runtime.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SetReadyBeans {
    private Logger logger = LoggerFactory.getLogger(SetReadyBeans.class);

    @Bean
    public Connector surveyReady(){
        return integrationContext -> {
            return integrationContext;
        };
    }
}
