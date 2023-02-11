package com.example.androidcrudapplication2;

public class ModelClass {

    private Integer id;
    private String name;
    private String email;
    private String contact;

    public ModelClass(String name, String email, String contact) {
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

    public ModelClass(Integer id, String name, String email, String contact) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
