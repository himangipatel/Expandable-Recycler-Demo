package com.expandablerecycleview.model;

/**
 * Created by Himangi on 15/7/17
 */

public class Child {

    private String fName;
    private String lName;

    public Child(String fName, String lName) {
        this.fName = fName;
        this.lName = lName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }
}
