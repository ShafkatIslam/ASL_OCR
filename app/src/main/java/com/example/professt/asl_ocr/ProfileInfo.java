package com.example.professt.asl_ocr;

public class ProfileInfo {

    Integer id;
    String name;
    String company;
    boolean select;

    public ProfileInfo(Integer id, String name, String company, boolean select) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.select = select;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
