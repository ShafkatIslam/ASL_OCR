package com.example.professt.asl_ocr;

import org.apache.commons.lang3.StringUtils;

public class Profile {

    private Integer id;
    private String name;
    private String jobTitle;
    private String company;
    private String primaryContactNumber;
    private String secondaryContactNumber;
    private String email;
    private String address;
    private String website;
    private String fax;

    public Profile(String name,
                   String jobTitle,
                   String company,
                   String primaryContactNumber,
                   String email,
                   String address,
                   String secondaryContactNumber,
                   String website,
                   String fax) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.company = company;
        this.primaryContactNumber = primaryContactNumber;
        this.email = email;
        this.address = address;
        this.secondaryContactNumber = secondaryContactNumber;
        this.website = website;
        this.fax = fax;
    }

    public boolean isValid(){
        boolean isValid = StringUtils.isNotBlank(name)
                && StringUtils.isNotBlank(company);
        return isValid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPrimaryContactNumber() {
        return primaryContactNumber;
    }

    public void setPrimaryContactNumber(String primaryContactNumber) {
        this.primaryContactNumber = primaryContactNumber;
    }

    public String getSecondaryContactNumber() {
        return secondaryContactNumber;
    }

    public void setSecondaryContactNumber(String secondaryContactNumber) {
        this.secondaryContactNumber = secondaryContactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
