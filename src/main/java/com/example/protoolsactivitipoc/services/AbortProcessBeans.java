package com.example.protoolsactivitipoc.services;

import org.activiti.api.process.runtime.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.util.Map;

public class AbortProcessBeans {
    private Logger logger = LoggerFactory.getLogger(AbortProcessBeans.class);
    @Bean
    public Connector endProcess(){
        return integrationContext -> {
            logger.info("\t >> Aborting Process ... <<  ");
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            // Contenu à analyser
            String surveyName = (String) inBoundVariables.get("surveyName");
            logger.info("\t \t >> Failed to create survey " + surveyName);
            return integrationContext;
        };
    }

}
