package com.example.blooddonationapp.model;

public class User {
    String name,id,bloodGroups,email,idno,phNo,profilePictureUrl,
    search,type;

    public User() {
    }

    public User(String name, String id, String bloodGroups, String email, String idno, String phNo, String profilePictureUrl, String search, String type) {
        this.name = name;
        this.id = id;
        this.bloodGroups = bloodGroups;
        this.email = email;
        this.idno = idno;
        this.phNo = phNo;
        this.profilePictureUrl = profilePictureUrl;
        this.search = search;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBloodGroups() {
        return bloodGroups;
    }

    public void setBloodGroups(String bloodGroups) {
        this.bloodGroups = bloodGroups;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
