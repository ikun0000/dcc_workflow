package com.example.dccworkflow.entity;

import javax.persistence.*;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String clientAddress;

    private String managerName;

    private String phone1;

    private String phone2;

    private String phone3;

    private String company;

    private String taxNumber;

    @ManyToOne
    private Brand brand;

    private String city;

    private Integer clientState;

    private String clientEmail;

    private String backupField1;

    private String backupField12;

    private String backupField13;

    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getClientState() {
        return clientState;
    }

    public void setClientState(Integer clientState) {
        this.clientState = clientState;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getBackupField1() {
        return backupField1;
    }

    public void setBackupField1(String backupField1) {
        this.backupField1 = backupField1;
    }

    public String getBackupField12() {
        return backupField12;
    }

    public void setBackupField12(String backupField12) {
        this.backupField12 = backupField12;
    }

    public String getBackupField13() {
        return backupField13;
    }

    public void setBackupField13(String backupField13) {
        this.backupField13 = backupField13;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }
}
