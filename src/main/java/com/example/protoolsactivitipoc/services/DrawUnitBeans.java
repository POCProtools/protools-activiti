package com.example.protoolsactivitipoc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.api.process.runtime.connector.Connector;
import org.activiti.engine.impl.util.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class DrawUnitBeans {
    private Logger logger = LoggerFactory.getLogger(SaveSurveyBeans.class);

    @Bean
    public Connector drawUnit(){
        return integrationContext -> {
            // Recup variables
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            // Contenu à analyser
            HttpClient client = HttpClient.newHttpClient();
            String url = "https://crabe.dev.insee.io/persons/sample/1" ;
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
            logger.info("\t \t Drawn unit : "+ response.body());
            integrationContext.addOutBoundVariable("unit",response.body());
            return integrationContext;
        };
    }
}
