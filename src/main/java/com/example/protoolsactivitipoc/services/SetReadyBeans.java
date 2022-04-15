package com.example.protoolsactivitipoc.services;

import org.activiti.api.process.runtime.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Component
public class SetReadyBeans {
    private Logger logger = LoggerFactory.getLogger(SetReadyBeans.class);

    @Bean
    public Connector surveyReady(){
        return integrationContext -> {
            HttpClient client = HttpClient.newHttpClient();
            // Recup variables
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            String surveyID = (String) inBoundVariables.get("idSurvey") ;
            String url = "https://coleman.dev.insee.io/survey/"+ String.valueOf(surveyID)+ "/ready";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = null;
            try {
                response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.info("\t \t >>> Survey set state to ready : " + response.body());
            return integrationContext;
        };
    }
}
