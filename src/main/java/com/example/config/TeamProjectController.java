package com.example.spa.controller;

import com.example.spa.entity.TeamProjectEntity;
import com.example.spa.service.TeamProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamProjectController { // 팀 프로젝트 생성

    @Autowired
    private TeamProjectService teamProjectService;

    @PostMapping("/create")
    public ResponseEntity<TeamProjectEntity> createTeamProject(@RequestBody TeamProjectEntity teamProject) {
        TeamProjectEntity createdTeamProject = teamProjectService.createTeamProject(teamProject);
        return new ResponseEntity<>(createdTeamProject, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeamProjectEntity>> getAllTeamProjects() {
        List<TeamProjectEntity> teamProjects = teamProjectService.getAllTeamProjects();
        return new ResponseEntity<>(teamProjects, HttpStatus.OK);
    }
}
