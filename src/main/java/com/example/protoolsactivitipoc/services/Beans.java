package com.example.protoolsactivitipoc.services;

import org.activiti.api.process.runtime.connector.Connector;
import org.activiti.api.process.runtime.events.ProcessCompletedEvent;
import org.activiti.api.process.runtime.events.listener.ProcessRuntimeEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class Beans {

    private Logger logger = LoggerFactory.getLogger(Beans.class);

    @Bean
    public Connector processTextConnector(){
        return integrationContext -> {
            // Recup variables
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            // Contenu à analyser
            String contentToProcess = (String) inBoundVariables.get("content");
            // Logic Here to decide if content is approved or not
            if (contentToProcess.contains("panda")) {
                logger.info("> Approving content: " + contentToProcess);
                integrationContext.addOutBoundVariable("approved",
                        true);
            } else {
                logger.info("> Discarding content: " + contentToProcess);
                integrationContext.addOutBoundVariable("approved",
                        false);
            }
            return integrationContext;
        };
    }

    @Bean
    public Connector discardTextConnector(){
        return integrationContext -> {
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            String toDiscard = (String) inBoundVariables.get("content");
            toDiscard += " -- There is no panda -- ";
            integrationContext.addOutBoundVariable("content",toDiscard);
            logger.info("Final result : "+ toDiscard);
            return integrationContext;
        };
    }

    @Bean
    public Connector tagTextConnector(){
        return integrationContext -> {
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            String toTag = (String) inBoundVariables.get("content");
            toTag += " -- :) -- ";
            integrationContext.addOutBoundVariable("content",toTag);
            logger.info("Final result : "+ toTag);
            return integrationContext;
        };
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessCompletedEvent> processCompletedListener() {
        return processCompleted -> logger.info(">>> Process Completed:  We can send a notification to the initiator: " + processCompleted.getEntity().getInitiator());
    }

}
