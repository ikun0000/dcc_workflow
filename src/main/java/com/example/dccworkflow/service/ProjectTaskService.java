package com.example.dccworkflow.service;

import com.example.dccworkflow.entity.ProjectForm;
import com.example.dccworkflow.entity.User;
import com.example.dccworkflow.exception.ProjectNotFoundException;
import com.example.dccworkflow.exception.UserNotFoundException;
import com.example.dccworkflow.repository.ProjectFormRepository;
import com.example.dccworkflow.repository.UserRepository;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ProjectTaskService {
    private TaskService taskService;
    private RuntimeService runtimeService;
    private HistoryService historyService;
    private ProjectFormRepository projectFormRepository;
    private UserRepository userRepository;


    public ProjectTaskService(TaskService taskService,
                              RuntimeService runtimeService,
                              HistoryService historyService,
                              ProjectFormRepository projectFormRepository,
                              UserRepository userRepository) {
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.historyService = historyService;
        this.projectFormRepository = projectFormRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProjectForm setExecutionEngineerContinue(Long projectFormId,
                                            Long userId) throws ProjectNotFoundException, UserNotFoundException {
        ProjectForm projectForm = projectFormRepository.findById(projectFormId)
                .orElseThrow(ProjectNotFoundException::new);

        // 指定工程师
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        projectForm.setExecuteEngineer(user);

        // 指定任务负责人和继续任务
        Map<String, Object> processVars = new HashMap<>();
        processVars.put("engineer", user.getUsername());
        Task task = taskService.createTaskQuery()
                .processInstanceId(projectForm.getProcessInstanceId())
                .taskName("人力资源准备")
                .singleResult();
        // 先删除之前定义的变量
        taskService.removeVariable(task.getId(), "engineer");
        runtimeService.removeVariable(projectForm.getProcessInstanceId(), "engineer");
        // 完成任务并设置流程变量
        taskService.complete(task.getId(), processVars);

        return projectForm;
    }

    @Transactional
    public ProjectForm setMaterialFinishContinue(Long projectFormId) throws ProjectNotFoundException {
        ProjectForm projectForm = projectFormRepository.findById(projectFormId)
                .orElseThrow(ProjectNotFoundException::new);

        // 完成物料准备
        projectForm.setMaterialArrangement(true);

        // 指定任务负责人和继续任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(projectForm.getProcessInstanceId())
                .taskName("物料资源准备")
                .singleResult();
        // 完成任务并设置流程变量
        taskService.complete(task.getId());

        return projectForm;
    }

    @Transactional
    public ProjectForm setInstalledContinue(Long projectFormId) throws ProjectNotFoundException {
        ProjectForm projectForm = projectFormRepository.findById(projectFormId)
                .orElseThrow(ProjectNotFoundException::new);


        // 指定任务负责人和继续任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(projectForm.getProcessInstanceId())
                .taskName("安装")
                .singleResult();
        // 完成任务并设置流程变量
        taskService.complete(task.getId());

        return projectForm;
    }

    @Transactional
    public ProjectForm setProjectFormContinue(Long projectFormId) throws ProjectNotFoundException {
        ProjectForm projectForm = projectFormRepository.findById(projectFormId)
                .orElseThrow(ProjectNotFoundException::new);


        // 指定任务负责人和继续任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(projectForm.getProcessInstanceId())
                .taskName("交付工单")
                .singleResult();
        // 完成任务并设置流程变量
        taskService.complete(task.getId());

        return projectForm;
    }


    @Transactional(readOnly = true)
    public List<ProjectForm> getUnfinishTaskProject(String assignee) {
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();

        List<Long> businessKeys = taskList.stream().map(task -> Long.parseLong(task.getBusinessKey())).toList();
        return projectFormRepository.findAllById(businessKeys);
    }

}
