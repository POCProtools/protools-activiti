package com.example.protoolsactivitipoc.services;

import org.activiti.api.process.runtime.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CheckSurveyInfoBeans {
    private Logger logger = LoggerFactory.getLogger(CheckSurveyInfoBeans.class);

    @Bean
    public Connector checkSampleSize(){
        return integrationContext -> {
            logger.info("\t >> Checking sample Size  ");
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            // Contenu à analyser
            String sampleSize = (String) inBoundVariables.get("sampleSize");
            int sampleSizeInt = Integer.parseInt(sampleSize);
            if (sampleSizeInt<5){
                integrationContext.addOutBoundVariable("surveyValid",false);
                logger.info("\t >> ERROR : Sample Size too small");
                logger.info("\t >> Process will be aborted");
            } else {
                integrationContext.addOutBoundVariable("surveyValid",true);
            }
            return integrationContext;
        };
    }
}
