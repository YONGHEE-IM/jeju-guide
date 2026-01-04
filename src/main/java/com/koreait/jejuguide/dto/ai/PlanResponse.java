package com.koreait.jejuguide.dto.ai;

import java.util.List;

public class PlanResponse {
    private int days;
    private String style;
    private String companion;
    private String budget;
    private List<DayPlan> plans;

    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
    public String getCompanion() { return companion; }
    public void setCompanion(String companion) { this.companion = companion; }
    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }
    public java.util.List<DayPlan> getPlans() { return plans; }
    public void setPlans(java.util.List<DayPlan> plans) { this.plans = plans; }
}
