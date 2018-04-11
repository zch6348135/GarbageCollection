package com.maruonan.garbagecollection.bean;

import org.litepal.crud.DataSupport;

/**
 * @author yf_zch
 * @version 1.0.0
 */
public class ApptBean extends DataSupport {

    private int id;

    private String type;
    private String weight;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
