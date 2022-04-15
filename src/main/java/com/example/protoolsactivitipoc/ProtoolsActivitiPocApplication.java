package com.example.protoolsactivitipoc;

import com.example.protoolsactivitipoc.util.SecurityUtil;
import com.example.protoolsactivitipoc.util.Utils;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ProtoolsActivitiPocApplication implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(ProtoolsActivitiPocApplication.class);

	@Autowired
	private ProcessRuntime processRuntime;

	// @Autowired
	// private SecurityUtil securityUtil;

	public static void main(String[] args) {
		SpringApplication.run(ProtoolsActivitiPocApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// securityUtil.logInAs("system");

		Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
		logger.info("> Available Process definitions: " + processDefinitionPage.getTotalItems());
		for (ProcessDefinition pd : processDefinitionPage.getContent()) {
			logger.info("\t > Process definition: " + pd);
		}

	}

}
