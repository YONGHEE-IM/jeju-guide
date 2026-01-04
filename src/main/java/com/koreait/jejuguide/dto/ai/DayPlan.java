package com.koreait.jejuguide.dto.ai;

import java.util.List;

public class DayPlan {
    private int day;
    private String title;
    private java.util.List<Activity> activities;

    public int getDay() { return day; }
    public void setDay(int day) { this.day = day; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public java.util.List<Activity> getActivities() { return activities; }
    public void setActivities(java.util.List<Activity> activities) { this.activities = activities; }
}
