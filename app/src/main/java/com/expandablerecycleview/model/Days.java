package com.expandablerecycleview.model;

/**
 * Created by Himangi on 26/7/17
 */

public class Days {

    private String day;
    private boolean isSelected;

    public Days(String day, boolean isSelected) {
        this.day = day;
        this.isSelected = isSelected;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
