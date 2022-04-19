package com.example.protoolsactivitipoc.services;

import com.example.protoolsactivitipoc.beans.Person;
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
            logger.info("\t >> Create account for unit");
            HttpClient client = HttpClient.newHttpClient();

            // Recup variables
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            // Contenu à analyser
            String unit = (String) inBoundVariables.get("unit");
            String surveyID = (String) inBoundVariables.get("idSurvey") ;
            int count = (int) inBoundVariables.get("count");
            logger.info("\t \t >>> Create Account for unit: " + unit + "for survey: "+ surveyID);

            Gson gson = new Gson();
            Person[] map = gson.fromJson(unit,Person[].class);
            Person person = map[0];
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

            logger.info("\t \t Response Code: " + String.valueOf(response.statusCode()));
            integrationContext.addOutBoundVariable("count", count+1);

            return integrationContext;
        };
    }
}
