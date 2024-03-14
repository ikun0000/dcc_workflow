package com.example.dccworkflow.service;

import com.example.dccworkflow.dto.ProjectFormDto;
import com.example.dccworkflow.entity.*;
import com.example.dccworkflow.enums.ProjectState;
import com.example.dccworkflow.exception.ClientNotFoundException;
import com.example.dccworkflow.exception.ProjectNotFoundException;
import com.example.dccworkflow.repository.ClientRepository;
import com.example.dccworkflow.repository.ProjectFileRepository;
import com.example.dccworkflow.repository.ProjectFormRepository;
import com.example.dccworkflow.repository.ProjectLogRepository;
import com.example.dccworkflow.utils.LikeWrap;
import com.querydsl.core.BooleanBuilder;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectManagementService {
    private final static String FLOW_KEY = "projectFlow";

    private ProjectFormRepository projectFormRepository;
    private ProjectFileRepository projectFileRepository;
    private ProjectLogRepository projectLogRepository;
    private ClientRepository clientRepository;
    private SerialGenerator serialGenerator;
    private HistoryService historyService;

    private RuntimeService runtimeService;
    private TaskService taskService;

    private final String projectFileRoot;


    public ProjectManagementService(ProjectFormRepository projectFormRepository,
                                    ProjectFileRepository projectFileRepository,
                                    ProjectLogRepository projectLogRepository,
                                    ClientRepository clientRepository,
                                    SerialGenerator serialGenerator,
                                    HistoryService historyService,
                                    RuntimeService runtimeService,
                                    TaskService taskService,
                                    @Value("${project-file-root}") String projectFileRoot) {
        this.projectFormRepository = projectFormRepository;
        this.projectFileRepository = projectFileRepository;
        this.projectLogRepository = projectLogRepository;
        this.clientRepository = clientRepository;
        this.serialGenerator = serialGenerator;
        this.historyService = historyService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.projectFileRoot = projectFileRoot;
    }

    @Transactional
    public ProjectForm startProject(User handler,
                                    Long clientId,
                                    Long subProjectTypeId,
                                    String constructionTeamPhone,
                                    LocalDate visitDate,
                                    LocalDate deliveryDate) throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(ClientNotFoundException::new);

        ProjectForm projectForm = new ProjectForm();

        // 项目负责人
        projectForm.setSerialNumber(serialGenerator.getSeralNum(client.getBrand().getRef()));
        projectForm.setHandler(handler);

        // 克隆一份客户信息
        projectForm.setClient(client);
        projectForm.setClientNum(client.getClientId());
        projectForm.setClientName(client.getClientName());
        projectForm.setClientAddress(client.getClientAddress());
        projectForm.setBrand(client.getBrand().getName());
        projectForm.setCity(client.getCity());
        projectForm.setManagerPhone(client.getPhone1());

        // 项目类型
        SubProjectType subProjectType = new SubProjectType();
        subProjectType.setId(subProjectTypeId);
        projectForm.setSubProjectType(subProjectType);

        // 施工队电话，上门时间，交付时间
        projectForm.setConstructionTeamPhone(constructionTeamPhone);
        projectForm.setVisitDate(visitDate);
        projectForm.setDeliveryDate(deliveryDate);

        // 创建时间和最后修改时间
        projectForm.setCreateDateTime(LocalDateTime.now());
        projectForm.setLastModifyDateTime(LocalDateTime.now());

        // 项目状态
        projectForm.setFinished(false);
        projectForm.setProjectState(ProjectState.NORMAL.getCode());

        // 创建项目文件存放目录，使用项目序列号
        File projectDir = new File(projectFileRoot +
                File.separator +
                projectForm.getSerialNumber().replaceAll("\\.", "_"));
        projectDir.mkdir();

        // 保存
        projectForm = projectFormRepository.save(projectForm);

        // 启动流程实例，关联业务key（表ID）
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FLOW_KEY,
                projectForm.getId().toString());

        // 记录运行实例ID和流程定义
        projectForm.setProcessInstanceId(processInstance.getId());
        projectForm.setProcessDefinitionId(processInstance.getProcessDefinitionId());

        // 如果填写了上门时间，立刻进入准备步骤
        if (visitDate != null) {
            Task task = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            taskService.setAssignee(task.getId(), handler.getUsername());
            taskService.complete(task.getId());
        }

        return projectForm;
    }

    @Transactional
    public void deleteProject(Long projectFormId) {
        Optional<ProjectForm> projectForm = projectFormRepository.findById(projectFormId);

        if (projectForm.isEmpty()) {
            return;
        }

        // 删除流程实例
        String processInstanceId = projectForm.get().getProcessInstanceId();
        projectFormRepository.delete(projectForm.get());

        // 删除项目文件目录
        File projectDir = new File(projectFileRoot +
                File.separator +
                projectForm.get().getSerialNumber().replaceAll("\\.", "_"));
        projectDir.delete();

        runtimeService.deleteProcessInstance(processInstanceId, "");
    }

    @Transactional
    public ProjectForm updateProjectFormInfo(Long id,
                                             String brand,
                                             String city,
                                             String clientName,
                                             String clientAddress,
                                             String managePhone,
                                             String constructionPhone,
                                             LocalDate visitDate,
                                             LocalDate deliveryDate,
                                             String note) throws ProjectNotFoundException {
        ProjectForm projectForm = getProjectForm(id);

        if (brand != null) projectForm.setBrand(brand);
        if (city != null) projectForm.setCity(city);
        if (clientName != null) projectForm.setClientName(clientName);
        if (clientAddress != null) projectForm.setClientAddress(clientAddress);
        if (managePhone != null) projectForm.setManagerPhone(managePhone);
        if (constructionPhone != null) projectForm.setConstructionTeamPhone(constructionPhone);
        if (visitDate != null) projectForm.setVisitDate(visitDate);
        if (deliveryDate != null) projectForm.setDeliveryDate(deliveryDate);
        if (note != null) projectForm.setNote(note);

        return projectForm;
    }

    @Transactional(readOnly = true)
    public Page<ProjectFormDto> getProjectFormDto(Long handlerId,
                                                  String search,
                                                  Long projectTypeId,
                                                  Long subProjectTypeId,
                                                  Integer projectState,
                                                  Pageable pageable) {
        QProjectForm qProjectForm = QProjectForm.projectForm;

        BooleanBuilder whereCase = new BooleanBuilder();

        if (handlerId != null && handlerId > 0) {
            whereCase.and(qProjectForm.handler.id.eq(handlerId));
        }

        if (search != null && !search.isBlank()) {
            whereCase.andAnyOf(qProjectForm.serialNumber.like(LikeWrap.like(search)),
                    qProjectForm.clientNum.like(LikeWrap.like(search)),
                    qProjectForm.clientName.like(LikeWrap.like(search)),
                    qProjectForm.clientAddress.like(LikeWrap.like(search)),
                    qProjectForm.city.like(LikeWrap.like(search)),
                    qProjectForm.brand.like(LikeWrap.like(search)));
        }

        if (projectState != null && projectState > 0) {
            whereCase.and(qProjectForm.projectState.eq(projectState));
        }
        if (projectTypeId != null && projectTypeId > 0) {
            whereCase.and(qProjectForm.subProjectType.projectType.id.eq(projectTypeId));
        }
        if (subProjectTypeId != null && subProjectTypeId > 0) {
            whereCase.and(qProjectForm.subProjectType.id.eq(subProjectTypeId));
        }

        Page<ProjectForm> projectFormPage = projectFormRepository.findAll(whereCase, pageable);

        List<ProjectFormDto> content = projectFormPage.getContent()
                .stream().map(projectForm -> {
                    ProjectFormDto dto = new ProjectFormDto();
                    dto.setId(projectForm.getId());
                    dto.setSerialNumber(projectForm.getSerialNumber());
                    dto.setBrand(projectForm.getBrand());
                    dto.setCity(projectForm.getCity());
                    dto.setClientNum(projectForm.getClientNum());
                    dto.setClientName(projectForm.getClientName());
                    dto.setClientAddress(projectForm.getClientAddress());

                    dto.setProjectType(projectForm.getSubProjectType().getProjectType().getName());
                    dto.setSubProjectType(projectForm.getSubProjectType().getName());

                    dto.setFinished(projectForm.getFinished());
                    dto.setProjectState(ProjectState.of(projectForm.getProjectState()).getMsg());

                    // 任务所处的流程节点
                    String activityName = taskService.createTaskQuery()
                            .processInstanceId(projectForm.getProcessInstanceId())
                            .list()
                            .stream().map(task -> task.getName())
                            .collect(Collectors.joining(", "));
                    dto.setActivityName(activityName);

                    dto.setCreateDateTime(projectForm.getCreateDateTime());

                    return dto;
                }).toList();

        return new PageImpl<>(content, pageable, projectFormPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ProjectForm getProjectForm(Long id) throws ProjectNotFoundException {
        return projectFormRepository.findById(id)
                .orElseThrow(ProjectNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<HistoricTaskInstance> getProjectTaskHistory(Long projectFormId) throws ProjectNotFoundException {
        ProjectForm projectForm = getProjectForm(projectFormId);

         return historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(projectForm.getProcessInstanceId())
                .orderByHistoricTaskInstanceStartTime()
                .desc()
                .list();
    }
}
