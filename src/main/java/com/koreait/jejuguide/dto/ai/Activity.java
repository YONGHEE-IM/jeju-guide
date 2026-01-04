package com.koreait.jejuguide.dto.ai;

public class Activity {
    private String time;
    private String place;
    private String description;
    private String category;

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public static Activity of(String time, String place, String description, String category){
        Activity a = new Activity();
        a.setTime(time); a.setPlace(place); a.setDescription(description); a.setCategory(category);
        return a;
    }
}
