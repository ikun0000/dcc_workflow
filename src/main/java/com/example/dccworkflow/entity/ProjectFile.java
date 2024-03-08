package com.example.dccworkflow.entity;

import javax.persistence.*;

@Entity
public class ProjectFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String accessUrl;

    private String physical;

    @ManyToOne
    private ProjectForm projectForm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getPhysical() {
        return physical;
    }

    public void setPhysical(String physical) {
        this.physical = physical;
    }

    public ProjectForm getProjectForm() {
        return projectForm;
    }

    public void setProjectForm(ProjectForm projectForm) {
        this.projectForm = projectForm;
    }
}
