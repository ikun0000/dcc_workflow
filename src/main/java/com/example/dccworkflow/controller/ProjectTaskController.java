package com.example.dccworkflow.controller;

import com.example.dccworkflow.entity.User;
import com.example.dccworkflow.exception.ProjectNotFoundException;
import com.example.dccworkflow.exception.UserNotFoundException;
import com.example.dccworkflow.service.ProjectManagementService;
import com.example.dccworkflow.service.ProjectTaskService;
import com.example.dccworkflow.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/projectTask")
public class ProjectTaskController {
    private ProjectTaskService projectTaskService;
    private ProjectManagementService projectManagementService;
    private UserService userService;

    public ProjectTaskController(ProjectTaskService projectTaskService,
                                 ProjectManagementService projectManagementService,
                                 UserService userService) {
        this.projectTaskService = projectTaskService;
        this.projectManagementService = projectManagementService;
        this.userService = userService;
    }

    @GetMapping("/listHRproject")
    @PreAuthorize("hasAuthority('edit_engineer')")
    public String listHRproject(Model model) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        model.addAttribute("unFinishTask", projectTaskService.getUnfinishTaskProject(user.getUsername()));

        return "projectTask/hrTaskProject";
    }

    @GetMapping("/setEngineerPage")
    @PreAuthorize("hasAuthority('edit_engineer')")
    public String setEngineerPage(@RequestParam Long id,
                                  Model model) throws ProjectNotFoundException {
        model.addAttribute("projectForm", projectManagementService.getProjectForm(id));
        model.addAttribute("engineers", userService.getUserWithRole("工程师"));
        return "projectTask/setProjectEngineerPage";
    }

    @PostMapping("/setEngineerContinue")
    @PreAuthorize("hasAuthority('edit_engineer')")
    public RedirectView setEngineerContinue(@RequestParam Long projectFormId,
                                            @RequestParam Long userId) throws UserNotFoundException, ProjectNotFoundException {
        projectTaskService.setExecutionEngineerContinue(projectFormId, userId);

        return new RedirectView("/projectTask/listHRproject");
    }

    @GetMapping("/listWarehouseProject")
    @PreAuthorize("hasAuthority('edit_material')")
    public String listWarehouseProject(Model model) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        model.addAttribute("unFinishTask", projectTaskService.getUnfinishTaskProject(user.getUsername()));
        return "projectTask/warehouseTaskProject";
    }

    @GetMapping("/setMaterialPage")
    @PreAuthorize("hasAuthority('edit_material')")
    public String setMaterialPage(@RequestParam Long id,
                                  Model model) throws ProjectNotFoundException {
        model.addAttribute("projectForm", projectManagementService.getProjectForm(id));
        return "projectTask/setMaterialPage";
    }

    @PostMapping("/setMaterial")
    @PreAuthorize("hasAuthority('edit_material')")
    public RedirectView  setMaterial(@RequestParam Long projectFormId) throws ProjectNotFoundException {
        projectTaskService.setMaterialFinishContinue(projectFormId);
        return new RedirectView("/projectTask/listWarehouseProject");
    }


    @GetMapping("/listEngineerProjectPage")
    @PreAuthorize("hasAuthority('edit_project_engine')")
    public String listEngineerProject(Model model) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        model.addAttribute("unFinishTask", projectTaskService.getUnfinishTaskProject(user.getUsername()));
        return "projectTask/engineerTaskProject";
    }

    @GetMapping("/setProjectInstalledPage")
    @PreAuthorize("hasAuthority('edit_project_engine')")
    public String setProjectInstalledPage(@RequestParam Long id,
                                        Model model) throws ProjectNotFoundException {
        model.addAttribute("projectForm", projectManagementService.getProjectForm(id));
        model.addAttribute("taskList", projectManagementService.getTaskByProjectFormId(id));
        return "projectTask/setProjectInstalledPage";
    }

    @PostMapping("/projectInstalled")
    @PreAuthorize("hasAuthority('edit_project_engine')")
    public RedirectView  projectInstalled(@RequestParam Long projectFormId) throws ProjectNotFoundException {
        projectTaskService.setInstalledContinue(projectFormId);
        return new RedirectView("/projectTask/listEngineerProjectPage");
    }

    @PostMapping("/sentProjectForm")
    @PreAuthorize("hasAuthority('edit_project_engine')")
    public RedirectView  sentProjectForm(@RequestParam Long projectFormId) throws ProjectNotFoundException {
        projectTaskService.setProjectFormContinue(projectFormId);
        return new RedirectView("/projectTask/listEngineerProjectPage");
    }
}
