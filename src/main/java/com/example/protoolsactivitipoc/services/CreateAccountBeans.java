package com.example.protoolsactivitipoc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
public class CreateAccountBeans {
    private Logger logger = LoggerFactory.getLogger(CreateAccountBeans.class);

    @Bean
    public Connector createAccount(){
        return integrationContext -> {
            HttpClient client = HttpClient.newHttpClient();
            // Recup variables
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            // Contenu à analyser
            String unit = (String) inBoundVariables.get("unit");
            String surveyID = (String) inBoundVariables.get("idSurvey") ;

            Gson gson = new Gson();
            Person person = gson.fromJson(unit,Person.class);

            logger.info("Create account : " + person.toString());
            int statusCode = 0;

            var values = new HashMap<String, Object>() {{
                put("email", person.getEmail());
                put("nom", person.getNom());
                put("prenom", person.getPrenom());
                put("telephone", person.getTelephone());
                put("id_survey",Long.parseLong(surveyID));
            }};
            var objectMapper = new ObjectMapper();
            String requestBody = null;
            try {
                requestBody = objectMapper
                        .writeValueAsString(values);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            requestBody = "[" + requestBody + "]";
            logger.info("Create account for unit: " + requestBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://annuaire.dev.insee.io/comptes/"+surveyID))
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

            logger.info(String.valueOf(response.statusCode()));
            statusCode = response.statusCode();

            return integrationContext;
        };
    }
}
