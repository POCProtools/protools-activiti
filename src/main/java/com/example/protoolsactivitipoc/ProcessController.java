package com.example.protoolsactivitipoc;

import com.example.protoolsactivitipoc.services.TaskRepresentation;
import com.example.protoolsactivitipoc.util.Utils;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ProcessController {
    private Logger logger = LoggerFactory.getLogger(ProcessController.class);
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @GetMapping(value = "/start-process/{processKey}")
    public String startProcess(@PathVariable String processKey){
        logger.info("> GET request to start the process: categorizeProcess");
        String content = Utils.pickRandomString();

        Map<String,Object> variables = new HashMap<String, Object>();
        variables.put("content",content);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

        logger.info("> Processing content: " + content + " at " + formatter.format(new Date()));

        runtimeService.startProcessInstanceByKey(processKey, variables);

        return(">>> Created Process Instance: "+ processKey);
    }

    @GetMapping("/get-tasks/{processID}")
    public List<TaskRepresentation> getTasks(@PathVariable String processID){

        List<Task> usertasks = taskService.createTaskQuery()
                .processInstanceId(processID)
                .list();

        return usertasks.stream()
                .map(task -> new TaskRepresentation(task.getId(), task.getName(), task.getProcessInstanceId()))
                .collect(Collectors.toList());

    }

    @GetMapping("/complete-task/{processID}")
    public String completeTask(@PathVariable String processID){
        Task task = taskService.createTaskQuery()
                .processInstanceId(processID)
                .singleResult();
        taskService.complete(task.getId());
        return(">>> Task : "+ processID+" completed!");
    }

}
