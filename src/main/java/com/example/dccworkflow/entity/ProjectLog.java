package com.example.dccworkflow.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ProjectLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime createLocalDateTime;

    @ManyToOne
    private User user;

    @ManyToOne
    private ProjectForm projectForm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateLocalDateTime() {
        return createLocalDateTime;
    }

    public void setCreateLocalDateTime(LocalDateTime createLocalDateTime) {
        this.createLocalDateTime = createLocalDateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProjectForm getProjectForm() {
        return projectForm;
    }

    public void setProjectForm(ProjectForm projectForm) {
        this.projectForm = projectForm;
    }
}
