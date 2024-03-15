package com.example.dccworkflow.controller;

import com.example.dccworkflow.dto.BTResult;
import com.example.dccworkflow.dto.ClientDto;
import com.example.dccworkflow.dto.ProjectFormDto;
import com.example.dccworkflow.dto.Result;
import com.example.dccworkflow.entity.User;
import com.example.dccworkflow.enums.ResultType;
import com.example.dccworkflow.exception.ProjectNotFoundException;
import com.example.dccworkflow.exception.VisitDateDefineException;
import com.example.dccworkflow.service.ClientService;
import com.example.dccworkflow.service.ProjectManagementService;
import org.activiti.engine.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/projectManagement")
public class ProjectManagementController {
    private ProjectManagementService projectManagementService;
    private ClientService clientService;


    public ProjectManagementController(ProjectManagementService projectManagementService,
                                       ClientService clientService) {
        this.projectManagementService = projectManagementService;
        this.clientService = clientService;
    }

    @GetMapping("/myProjectListPage")
    @PreAuthorize("hasAuthority('project_management')")
    public String myProjectPage() {
        return "myProject/myProjectListPage";
    }

    @GetMapping("/listProject.bt")
    @PreAuthorize("hasAuthority('project_management')")
    @ResponseBody
    public Result<BTResult<ProjectFormDto>> listProjectBT(@RequestParam String search,
                                                          @RequestParam(defaultValue = "1") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer size,
                                                          Integer projectState,
                                                          Long projectType,
                                                          Long subProjectType) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Page<ProjectFormDto> projectFormDto = projectManagementService.getProjectFormDto(user.getId(),
                search, projectType, subProjectType, projectState,
                PageRequest.of(page - 1, size,
                        Sort.by(Sort.Order.desc("id"))));

        return Result.of(ResultType.SUCCESS,
                BTResult.of(projectFormDto.getContent(), projectFormDto.getTotalElements()));
    }

    @PostMapping("/createProject")
    @PreAuthorize("hasAuthority('project_management')")
    public RedirectView createProject(@RequestParam Long clientId,
                                      Long subProjectTypeId,
                                      String constructionPhone,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate visitDate,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate deliveryDate) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        try {
            projectManagementService.startProject(user,
                    clientId, subProjectTypeId, constructionPhone, visitDate, deliveryDate);
        } catch (Exception e) {
            return new RedirectView("/projectManagement/myProjectListPage?error");
        }

        return new RedirectView("/projectManagement/myProjectListPage?success");
    }

    @GetMapping("/detail/{projectFormId}")
    @PreAuthorize("hasAuthority('project_management')")
    public String projectDetail(@PathVariable Long projectFormId,
                                Model model) throws ProjectNotFoundException {
        model.addAttribute("projectForm",
                projectManagementService.getProjectForm(projectFormId));
        model.addAttribute("projectHistoryList",
                projectManagementService.getProjectTaskHistory(projectFormId));

        try {
            List<Task> taskList = projectManagementService.getTaskByProjectFormId(projectFormId);
            model.addAttribute("taskList", taskList);
        } catch (ProjectNotFoundException e) {

        }
        return "myProject/projectDetail";
    }

    @PostMapping("/deleteProject")
    @PreAuthorize("hasAuthority('project_management')")
    public RedirectView deleteProject(@RequestParam Long projectFormId) {
        projectManagementService.deleteProject(projectFormId);
        return new RedirectView("/projectManagement/myProjectListPage");
    }

    @PostMapping("/setVisitDateContinue")
    @PreAuthorize("hasAuthority('project_management')")
    public RedirectView setVisitDateContinue(@RequestParam Long id,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate visitDate)
            throws VisitDateDefineException, ProjectNotFoundException {
        projectManagementService.setVisitDateAndContinueProcess(id, visitDate);
        return new RedirectView("/projectManagement/detail/" + id);
    }


    @PostMapping("/setDeliveryDate")
    @PreAuthorize("hasAuthority('project_management')")
    public RedirectView setDeliveryDate(@RequestParam Long id,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate deliveryDate)
            throws VisitDateDefineException, ProjectNotFoundException {
        projectManagementService.setDeliveryDate(id, deliveryDate);
        return new RedirectView("/projectManagement/detail/" + id);
    }

    @PostMapping("/setProjectNote")
    @PreAuthorize("hasAuthority('project_management')")
    public RedirectView setVisitDateContinue(@RequestParam Long id,
                                             String note) throws ProjectNotFoundException {
        projectManagementService.setProjectNote(id, note);
        return new RedirectView("/projectManagement/detail/" + id);
    }

    @PostMapping("/getPayForm")
    @PreAuthorize("hasAuthority('project_management')")
    public RedirectView getPayForm(@RequestParam Long id) throws ProjectNotFoundException {
        projectManagementService.finishGetPayForm(id);
        return new RedirectView("/projectManagement/detail/" + id);
    }

    @PostMapping("/getMoney")
    @PreAuthorize("hasAuthority('project_management')")
    public RedirectView getMoney(@RequestParam Long id) throws ProjectNotFoundException {
        projectManagementService.finishGetMoney(id);
        return new RedirectView("/projectManagement/detail/" + id);
    }
}
