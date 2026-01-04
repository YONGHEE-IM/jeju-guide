package com.koreait.jejuguide.dto.ai;

public class PlanRequest {
    private int days;
    private String style;
    private String companion;
    private String budget;

    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
    public String getCompanion() { return companion; }
    public void setCompanion(String companion) { this.companion = companion; }
    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }
}
