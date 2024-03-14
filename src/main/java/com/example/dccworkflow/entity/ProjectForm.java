package com.example.dccworkflow.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ProjectForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String clientNum;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String clientAddress;

    private String managerPhone;

    @ManyToOne
    private Client client;

    private String constructionTeamPhone;

    @ManyToOne
    private SubProjectType subProjectType;

    private LocalDate visitDate;

    private LocalDate deliveryDate;

    @ManyToOne
    private User executeEngineer;

    private Boolean materialArrangement;

    private String note;

    @OneToMany(mappedBy = "projectForm")
    private List<ProjectFile> projectFiles;

    private Boolean invoiceIssued;

    private LocalDate invoiceSubmitDate;

    private String invoiceNote;

    private Integer projectState;

    private Boolean finished;

    private LocalDateTime createDateTime;

    private LocalDateTime lastModifyDateTime;

    @ManyToOne
    private User handler;

    private String processInstanceId;

    private String processDefinitionId;

    @OneToMany(mappedBy = "projectForm")
    private List<ProjectLog> projectLogs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getClientNum() {
        return clientNum;
    }

    public void setClientNum(String clientNum) {
        this.clientNum = clientNum;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getConstructionTeamPhone() {
        return constructionTeamPhone;
    }

    public void setConstructionTeamPhone(String constructionTeamPhone) {
        this.constructionTeamPhone = constructionTeamPhone;
    }

    public SubProjectType getSubProjectType() {
        return subProjectType;
    }

    public void setSubProjectType(SubProjectType subProjectType) {
        this.subProjectType = subProjectType;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public User getExecuteEngineer() {
        return executeEngineer;
    }

    public void setExecuteEngineer(User executeEngineer) {
        this.executeEngineer = executeEngineer;
    }

    public Boolean getMaterialArrangement() {
        return materialArrangement;
    }

    public void setMaterialArrangement(Boolean materialArrangement) {
        this.materialArrangement = materialArrangement;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getInvoiceIssued() {
        return invoiceIssued;
    }

    public void setInvoiceIssued(Boolean invoiceIssued) {
        this.invoiceIssued = invoiceIssued;
    }

    public LocalDate getInvoiceSubmitDate() {
        return invoiceSubmitDate;
    }

    public void setInvoiceSubmitDate(LocalDate invoiceSubmitDate) {
        this.invoiceSubmitDate = invoiceSubmitDate;
    }

    public String getInvoiceNote() {
        return invoiceNote;
    }

    public void setInvoiceNote(String invoiceNote) {
        this.invoiceNote = invoiceNote;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDateTime getLastModifyDateTime() {
        return lastModifyDateTime;
    }

    public void setLastModifyDateTime(LocalDateTime lastModifyDateTime) {
        this.lastModifyDateTime = lastModifyDateTime;
    }

    public User getHandler() {
        return handler;
    }

    public void setHandler(User handler) {
        this.handler = handler;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public List<ProjectFile> getProjectFiles() {
        return projectFiles;
    }

    public void setProjectFiles(List<ProjectFile> projectFiles) {
        this.projectFiles = projectFiles;
    }

    public List<ProjectLog> getProjectLogs() {
        return projectLogs;
    }

    public void setProjectLogs(List<ProjectLog> projectLogs) {
        this.projectLogs = projectLogs;
    }

    public Integer getProjectState() {
        return projectState;
    }

    public void setProjectState(Integer projectState) {
        this.projectState = projectState;
    }
}
