package com.example.dccworkflow.dto;

import java.io.Serializable;

public class BrandDto implements Serializable {
    private Long id;
    private String name;
    private String ref;

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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
