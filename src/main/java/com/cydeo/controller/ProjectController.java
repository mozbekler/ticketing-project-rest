package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    @RolesAllowed("Manager")
    public ResponseEntity<ResponseWrapper> getProjects(){
        List<ProjectDTO> projectDTOList=projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("All projects successfully retrieved",projectDTOList, HttpStatus.OK));

    }

    @GetMapping("{code}")
    @RolesAllowed("Manager")
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable("code") String code){
        ProjectDTO projectDTO= projectService.getByProjectCode(code);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully retrieved",projectDTO,HttpStatus.OK));

    }

    @PostMapping
    @RolesAllowed({"Manager","Admin"})
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO){
        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseWrapper("Project is successfully created",HttpStatus.CREATED));
    }

    @PutMapping
    @RolesAllowed("Manager")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO){
        projectService.update(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully updated",HttpStatus.OK));
    }

    @DeleteMapping("/{projectCode}")
    @RolesAllowed("Manager")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectCode") String code){
        projectService.delete(code);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully deleted",HttpStatus.OK));
    }

    @GetMapping("/manager/project-status")
    @RolesAllowed("Manager")
    public ResponseEntity<ResponseWrapper> getProjectByManager(@PathVariable("manager") String managerName){
        List<ProjectDTO> projectDTOList=projectService.listAllProjectDetails();
        return ResponseEntity.ok(new ResponseWrapper("These projects are assigned to the manager",projectDTOList,HttpStatus.OK));
    }

    @PutMapping("/manager/complete/{projectCode}")
    @RolesAllowed("Manager")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode") String projectCode){
        projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("It is successfully completed", HttpStatus.OK));
    }






}
