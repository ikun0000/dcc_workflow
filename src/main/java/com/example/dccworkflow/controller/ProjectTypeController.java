package com.example.dccworkflow.controller;

import com.example.dccworkflow.dto.BTResult;
import com.example.dccworkflow.dto.ProjectTypeDto;
import com.example.dccworkflow.dto.Result;
import com.example.dccworkflow.dto.SubProjectTypeDto;
import com.example.dccworkflow.enums.ResultType;
import com.example.dccworkflow.service.ProjectTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/projectType")
public class ProjectTypeController {
    private ProjectTypeService projectTypeService;

    public ProjectTypeController(ProjectTypeService projectTypeService) {
        this.projectTypeService = projectTypeService;
    }

    @GetMapping("/parentTypePage")
    @PreAuthorize("hasAuthority('edit_project_type')")
    public String typePage() {
        return "projectType/parentTypePage";
    }

    @GetMapping("/parentType.bt")
    @PreAuthorize("hasAuthority('edit_project_type')")
    @ResponseBody
    public Result<BTResult<ProjectTypeDto>> parentTypeBT(@RequestParam String search,
                                                         @RequestParam(defaultValue = "1") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer size) {
        Page<ProjectTypeDto> projectTypeDtos = projectTypeService.getProjectTypeDto(search,
                PageRequest.of(page - 1, size,
                        Sort.by(Sort.Direction.DESC, "id")));

        return Result.of(ResultType.SUCCESS,
                BTResult.of(projectTypeDtos.getContent(), projectTypeDtos.getTotalElements()));
    }

    @PostMapping("/addParentType")
    @PreAuthorize("hasAuthority('edit_project_type')")
    public RedirectView addParentType(@RequestParam String name) {
        projectTypeService.addProjectType(name);
        return new RedirectView("/projectType/parentTypePage");
    }

    @PostMapping("/removeParentType")
    @PreAuthorize("hasAuthority('edit_project_type')")
    @ResponseBody
    public Result<Object> removeParentType(@RequestParam Long id) {
        try {
            projectTypeService.removePorjectType(id);
        } catch (Exception e) {
            return Result.of(ResultType.PARENT_PROJECT_TYPE_REF, null);
        }
        return Result.of(ResultType.SUCCESS, null);
    }

    @GetMapping("/projectType.json")
    @ResponseBody
    public Result<List<ProjectTypeDto>> projectTypeJSON() {
        Page<ProjectTypeDto> projectTypeDtos = projectTypeService.getProjectTypeDto(null, Pageable.unpaged());
        return Result.of(ResultType.SUCCESS, projectTypeDtos.getContent());
    }

    @GetMapping("/subProjectType.json")
    @ResponseBody
    public Result<List<SubProjectTypeDto>> subProjectTypeJSON(@RequestParam Long projectTypeId) {
        Page<SubProjectTypeDto> projectTypeDto = projectTypeService.getSubProjectTypeDto(projectTypeId, null,
                Pageable.unpaged());

        return Result.of(ResultType.SUCCESS, projectTypeDto.getContent());
    }

    @GetMapping("/subTypePage")
    @PreAuthorize("hasAuthority('edit_project_type')")
    public String subTypePage() {
        return "projectType/subTypePage";
    }

    @GetMapping("/subType.bt")
    @PreAuthorize("hasAuthority('edit_project_type')")
    @ResponseBody
    public Result<BTResult<SubProjectTypeDto>> subTypeBT(@RequestParam String search,
                                                         @RequestParam(defaultValue = "1") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer size) {
        Page<SubProjectTypeDto> subProjectTypeDtos = projectTypeService.getSubProjectTypeDto(null,
                search,
                PageRequest.of(page - 1, size,
                        Sort.by(Sort.Direction.DESC, "id")));

        return Result.of(ResultType.SUCCESS,
                BTResult.of(subProjectTypeDtos.getContent(), subProjectTypeDtos.getTotalElements()));
    }

    @PostMapping("/addSubProjectType")
    @PreAuthorize("hasAuthority('edit_project_type')")
    public RedirectView addSubProjectType(@RequestParam Long projectTypeId,
                                          @RequestParam String name) {
        projectTypeService.addSubProjectType(projectTypeId, name);
        return new RedirectView("/projectType/subTypePage");
    }

    @PostMapping("/removeSubProjectType")
    @PreAuthorize("hasAuthority('edit_project_type')")
    @ResponseBody
    public Result<Object> removeSubProjectType(@RequestParam Long id) {
        try {
            projectTypeService.removeSubProjectType(id);
        } catch (Exception e) {
            return Result.of(ResultType.SUB_PROJECT_TYPE_REF, null);
        }
        return Result.of(ResultType.SUCCESS, null);
    }
}
