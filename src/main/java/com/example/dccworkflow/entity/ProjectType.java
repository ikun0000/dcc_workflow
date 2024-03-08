package com.example.dccworkflow.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class ProjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "projectType")
    private Set<SubProjectType> subProjectTypes;

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

    public Set<SubProjectType> getSubProjectTypes() {
        return subProjectTypes;
    }

    public void setSubProjectTypes(Set<SubProjectType> subProjectTypes) {
        this.subProjectTypes = subProjectTypes;
    }
}
