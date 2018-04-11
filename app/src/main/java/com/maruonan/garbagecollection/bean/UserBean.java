package com.maruonan.garbagecollection.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class UserBean extends DataSupport {

    private int id;
    @Column(unique = true, defaultValue = "unkown")
    private String username;

    private String telNum;

    private String address;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
