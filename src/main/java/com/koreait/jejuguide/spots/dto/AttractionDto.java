package com.koreait.jejuguide.spots.dto;

public class AttractionDto {
    private String title;
    private String location;
    private String imageUrl;
    private String category;
    private String description;

    public AttractionDto() {}
    public AttractionDto(String title, String location, String imageUrl,
                         String category, String description) {
        this.title = title; this.location = location; this.imageUrl = imageUrl;
        this.category = category; this.description = description;
    }
    public String getTitle() { return title; }           public void setTitle(String title) { this.title = title; }
    public String getLocation() { return location; }     public void setLocation(String location) { this.location = location; }
    public String getImageUrl() { return imageUrl; }     public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCategory() { return category; }     public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
}
