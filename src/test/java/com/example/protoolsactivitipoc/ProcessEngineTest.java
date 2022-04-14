package com.example.protoolsactivitipoc;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProcessEngineTest {
    @Test
    public void givenXMLConfig_whenGetDefault_thenGotProcessEngine() {
        ProcessEngine processEngine
                = ProcessEngines.getDefaultProcessEngine();
        assertNotNull(processEngine);
        assertEquals("root", processEngine.getProcessEngineConfiguration()
                .getJdbcUsername());
    }
}
