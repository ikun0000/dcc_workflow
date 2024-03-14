package com.example.dccworkflow.service;

import com.example.dccworkflow.dto.ProjectTypeDto;
import com.example.dccworkflow.dto.SubProjectTypeDto;
import com.example.dccworkflow.entity.ProjectType;
import com.example.dccworkflow.entity.QProjectType;
import com.example.dccworkflow.entity.QSubProjectType;
import com.example.dccworkflow.entity.SubProjectType;
import com.example.dccworkflow.repository.ProjectTypeRepository;
import com.example.dccworkflow.repository.SubProjectTypeRepository;
import com.example.dccworkflow.utils.LikeWrap;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectTypeService {
    private ProjectTypeRepository projectTypeRepository;
    private SubProjectTypeRepository subProjectTypeRepository;

    public ProjectTypeService(ProjectTypeRepository projectTypeRepository, SubProjectTypeRepository subProjectTypeRepository) {
        this.projectTypeRepository = projectTypeRepository;
        this.subProjectTypeRepository = subProjectTypeRepository;
    }

    @Transactional
    public ProjectType addProjectType(String name) {
        ProjectType projectType = new ProjectType();
        projectType.setName(name);
        return projectTypeRepository.save(projectType);
    }

    @Transactional
    public void removePorjectType(Long id) {
        projectTypeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ProjectTypeDto> getProjectTypeDto(String search, Pageable pageable) {
        QProjectType qProjectType = QProjectType.projectType;

        BooleanBuilder whereCase = new BooleanBuilder();
        if (search != null && !search.isBlank()) {
            whereCase.or(qProjectType.name.like(LikeWrap.like(search)));
        }

        Page<ProjectType> projectTypePage = projectTypeRepository.findAll(whereCase, pageable);

        List<ProjectTypeDto> content = projectTypePage.getContent()
                .stream().map(projectType -> {
                    ProjectTypeDto dto = new ProjectTypeDto();
                    BeanUtils.copyProperties(projectType, dto);
                    return dto;
                })
                .toList();

        return new PageImpl<>(content, pageable, projectTypePage.getTotalElements());
    }

    @Transactional
    public SubProjectType addSubProjectType(Long projectTypeId, String name) {
        ProjectType projectType = new ProjectType();
        projectType.setId(projectTypeId);

        SubProjectType subProjectType = new SubProjectType();
        subProjectType.setName(name);
        subProjectType.setProjectType(projectType);

        return subProjectTypeRepository.save(subProjectType);
    }

    @Transactional
    public void removeSubProjectType(Long subProjectTypeId) {
        subProjectTypeRepository.deleteById(subProjectTypeId);
    }

    @Transactional(readOnly = true)
    public Page<SubProjectTypeDto> getSubProjectTypeDto(Long projectTypeId,
                                                        String search,
                                                        Pageable pageable) {
        QSubProjectType qSubProjectType = QSubProjectType.subProjectType;

        BooleanBuilder whereCase = new BooleanBuilder();
        if (projectTypeId != null) {
            whereCase.and(qSubProjectType.projectType.id.eq(projectTypeId));
        }

        if (search != null && !search.isBlank()) {
            whereCase.andAnyOf(qSubProjectType.name.like(LikeWrap.like(search)),
                    qSubProjectType.projectType.name.like(LikeWrap.like(search)));
        }

        Page<SubProjectType> subProjectTypePage = subProjectTypeRepository.findAll(whereCase, pageable);

        List<SubProjectTypeDto> content = subProjectTypePage.stream()
                .map(subProjectType -> {
                    SubProjectTypeDto dto = new SubProjectTypeDto();
                    BeanUtils.copyProperties(subProjectType, dto);
                    dto.setParentName(subProjectType.getProjectType().getName());
                    return dto;
                })
                .toList();

        return new PageImpl<>(content, pageable, subProjectTypePage.getTotalElements());
    }
}
