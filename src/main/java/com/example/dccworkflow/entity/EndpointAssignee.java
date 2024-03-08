package com.example.dccworkflow.entity;

import javax.persistence.*;

@Entity
public class EndpointAssignee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String processKey;

    private String assigneeVarName;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getAssigneeVarName() {
        return assigneeVarName;
    }

    public void setAssigneeVarName(String assigneeVarName) {
        this.assigneeVarName = assigneeVarName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
