package com.example.protoolsactivitipoc.services;

import org.activiti.api.process.runtime.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
            URL url = null;
            try {
                url = new URL("https://coleman.dev.insee.io/survey/"+ String.valueOf(surveyID)+ "/ready");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.getInputStream();
                logger.info("\t >> Survey set state to ready : " + con.getResponseCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return integrationContext;
        };
    }
}
