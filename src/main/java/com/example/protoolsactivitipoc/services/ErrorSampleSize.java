package com.example.protoolsactivitipoc.services;

import org.activiti.api.process.runtime.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

public class ErrorSampleSize {
    private Logger logger = LoggerFactory.getLogger(ErrorSampleSize.class);
    @Bean
    public Connector sampleSizeSmall(){
        return integrationContext -> {
            logger.info("\t >> Sample size error : Sample Size must be >5 ");
            logger.info("\t >> Process aborted ");
            return integrationContext;
        };
    }
}
