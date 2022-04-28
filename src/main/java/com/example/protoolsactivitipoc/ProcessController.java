package com.example.protoolsactivitipoc;

import com.example.protoolsactivitipoc.beans.Survey;
import com.example.protoolsactivitipoc.util.SecurityUtil;
import com.example.protoolsactivitipoc.util.Utils;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProcessController {
    private Logger logger = LoggerFactory.getLogger(ProcessController.class);
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping(value = "/start-categorize-process/")
    public String startProcess(){
        logger.info("> GET request to start the process: categorizeProcess");
        String content = Utils.pickRandomString();

        Map<String,Object> variables = new HashMap<String, Object>();
        variables.put("content",content);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

        logger.info("> Processing content: " + content + " at " + formatter.format(new Date()));

        runtimeService.startProcessInstanceByKey("categorizeProcess", variables);

        return(">>> Created Process Instance: "+ "categorizeProcess");
    }

    @GetMapping(value = "/start-process/{processKey}" )
    public String startProcess(@PathVariable String processKey){
        logger.info("> GET request to start the process: "+ processKey);

        runtimeService.startProcessInstanceByKey(processKey);
        List<ProcessInstance> liste = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processKey)
                .list();
        for (ProcessInstance l : liste){
            logger.info("Process Instance ID : " + l.getId());
        }

        return(">>> Created Process Instance: "+ processKey);
    }

    @GetMapping("/get-tasks/{processKey}")
    public void getTasks(@PathVariable String processKey) {
        securityUtil.logInAs("mailine");
        logger.info(">>> Get tasks <<<");
        List<org.activiti.engine.task.Task> taskInstances = taskService.createTaskQuery().processDefinitionKey(processKey).active().list();
        logger.info(taskInstances.toString());
        if (taskInstances.size() > 0) {
            for (Task t : taskInstances) {
                taskService.addCandidateGroup(t.getId(), "userTeam");
                logger.info("> Claiming task: " + t.getId());
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(t.getId()).build());
            }
        } else {
            logger.info("\t \t >> There are no task for me to work on.");
        }

    }

    @GetMapping("/complete-task/{processKey}")
    public void completeTaskA(@PathVariable String processKey, @RequestBody HashMap<String,Object> variables) {
        securityUtil.logInAs("mailine");
        List<org.activiti.engine.task.Task> taskInstances = taskService.createTaskQuery().processDefinitionKey(processKey).active().list();
        logger.info("> Completing task from process : " + processKey);
        logger.info("\t > Variables : " + variables.toString());
        if (taskInstances.size() > 0) {
            for (Task t : taskInstances) {
                taskService.addCandidateGroup(t.getId(), "userTeam");
                logger.info("> Claiming task: " + t.getId());
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(t.getId()).withVariables(variables).build());;
            }
        } else {
            logger.info("\t \t >> There are no task for me to complete");
        }
    }

}
