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
                taskService.addCandidateGroup(t.getId(), "GROUP_userTeam");
                logger.info("> Claiming task: " + t.getId());
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(t.getId()).build());
            }
        } else {
            logger.info("\t \t >> There are no task for me to work on.");
        }

    }

    @GetMapping("/complete-task/{taskID}")
    public void completeTaskA(@PathVariable String taskID, @RequestBody HashMap<String,Object> variables) {
        securityUtil.logInAs("system");
        logger.info("> Completing task: " + taskID);
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID).withVariables(variables).build());
    }

    @GetMapping(value = "/startProcess/createSurvey/{processKey}", consumes = "application/json")
    public String createSurveyPost(@PathVariable String processKey, @RequestBody Survey newSurvey) {
        securityUtil.logInAs("system");
        logger.info("\t >>> StartProcess createSurvey <<<");
        var values = new HashMap<String, Object>() ;
        try {
            logger.info(">> Survey: " + newSurvey.toString());
            values.put("name", newSurvey.getName());
            values.put("dateDeb", newSurvey.getDateDeb());
            values.put("dateEnd", newSurvey.getDateEnd());
            values.put("sampleSize", newSurvey.getSampleSize());

            logger.info("\t \t >>> Finished saving survey info into values");
        } catch (Error e) {
            logger.info("Error : " + e.getMessage());
        }
        runtimeService.startProcessInstanceByKey(processKey, values);
        List<ProcessInstance> liste = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processKey)
                .list();

        return (">>> Created Process Instance: " + processKey + " -- info: " + liste.toString());
    }

}
