package com.maruonan.garbagecollection.bean;

/**
 * @author yf_zch
 * @version 1.0.0
 */
public class TextBean {

    /**
     * text : 十斤以下
     */

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
