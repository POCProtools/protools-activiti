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
public class SaveSurveyBeans {

    private Logger logger = LoggerFactory.getLogger(SaveSurveyBeans.class);

    @Bean
    public Connector saveSurvey(){
        return integrationContext -> {
            logger.info("\t >> Service task Save Survey into Coleman");
            // Recup variables
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            // Contenu à analyser
            String surveyName = (String) inBoundVariables.get("surveyName");
            String dateDeb = (String) inBoundVariables.get("dateDeb");
            String dateEnd = (String) inBoundVariables.get("dateEnd");

            var values = new HashMap<String, String>() {{
                put("name", surveyName);
                put ("dateDeb", dateDeb);
                put("dateEnd", dateEnd);
            }};

            var objectMapper = new ObjectMapper();
            String requestBody = null;
            try {
                requestBody = objectMapper
                        .writeValueAsString(values);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://coleman.dev.insee.io/surveys/"))
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
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

            JSONObject jsonResponse = new JSONObject(response.body());
            logger.info("\t \t >>> Coleman response : " +jsonResponse);
            int idInt = jsonResponse.getInt("id");
            String idSurvey = String.valueOf(idInt);
            integrationContext.addOutBoundVariable("idSurvey",idSurvey);
            integrationContext.addOutBoundVariable("count",0);

            return integrationContext;
        };
    }

}
