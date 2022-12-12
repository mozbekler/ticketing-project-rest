package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping
    @RolesAllowed("Manager")
    public ResponseEntity<ResponseWrapper> getTasks(){
        List<TaskDTO> taskDTOList=taskService.listAllTasks();
        ResponseWrapper responseWrapper = new ResponseWrapper("All tasks successfully retrieved",taskDTOList, HttpStatus.OK);
        return ResponseEntity.ok(responseWrapper);
    }
    @GetMapping("/{taskId}")
    @RolesAllowed("Manager")
    public ResponseEntity<ResponseWrapper> getTaskById(@PathVariable("taskId") Long taskId){
        ResponseWrapper responseWrapper = new ResponseWrapper("The task is successfully retrieved",taskService.findById(taskId),HttpStatus.ACCEPTED);
        return ResponseEntity.ok(responseWrapper);
    }
    @PostMapping
    @RolesAllowed("Manager")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO task){
        taskService.save(task);
        ResponseWrapper responseWrapper = new ResponseWrapper("Task is successfully created",HttpStatus.CREATED);
        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(responseWrapper);
    }
    @DeleteMapping("/{id}")
    @RolesAllowed("Manager")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("id") Long id){
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully deleted",HttpStatus.OK));
    }
    @PutMapping
    @RolesAllowed("Manager")
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO task){
        taskService.update(task);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully updated",HttpStatus.OK));
    }

    @GetMapping("/employee/pending-tasks")
    @RolesAllowed("Employee")
    public ResponseEntity<ResponseWrapper> employeePendingTasks (){
        List<TaskDTO> taskDTOList= taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Tasks are successfully retrieved",taskDTOList,HttpStatus.OK));
    }

    @PutMapping("/employee/update")
    @RolesAllowed("Employee")
    public ResponseEntity<ResponseWrapper> employeeUpdateTasks(@RequestBody TaskDTO task){
        taskService.update(task);
        return ResponseEntity.ok(new ResponseWrapper("Tasks are successfully updated",HttpStatus.OK));
    }

    @GetMapping("/employee/archive")
    @RolesAllowed("Employee")
    public ResponseEntity<ResponseWrapper> employeeArchivedTasks(){
        List<TaskDTO> taskDTOList = taskService.listAllTasksByStatus(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Tasks are successfully retrieved",taskDTOList,HttpStatus.OK));

    }


}
