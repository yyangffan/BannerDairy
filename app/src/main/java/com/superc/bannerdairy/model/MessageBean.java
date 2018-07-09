package com.superc.bannerdairy.model;

import java.io.Serializable;

/**
 * 可以随意使用  有五个参数可以根据需求添加
 */

public class MessageBean implements Serializable{
    private String one;
    private String two;
    private String three;
    private String four;
    private String five;

    public MessageBean(String one) {
        this.one = one;
    }

    public MessageBean(String one, String two) {
        this.one = one;
        this.two = two;
    }

    public MessageBean(String one, String two, String three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }

    public MessageBean(String one, String two, String three, String four) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
    }

    public MessageBean(String one, String two, String three, String four, String five) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getFive() {
        return five;
    }

    public void setFive(String five) {
        this.five = five;
    }
}
