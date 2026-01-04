package com.koreait.jejuguide.spots.dto;

public class AttractionDetailDto {
    private String title;
    private String imageUrl;
    private String location;
    private String category;
    private String detail;   // 긴 설명
    private String hours;    // 운영시간
    private String fee;      // 입장료

    public AttractionDetailDto() {}
    public AttractionDetailDto(String title, String imageUrl, String location,
                               String category, String detail,
                               String hours, String fee) {
        this.title = title; this.imageUrl = imageUrl; this.location = location;
        this.category = category; this.detail = detail;
        this.hours = hours; this.fee = fee; 
    }
    public String getTitle(){ return title; }         public void setTitle(String title){ this.title = title; }
    public String getImageUrl(){ return imageUrl; }   public void setImageUrl(String imageUrl){ this.imageUrl = imageUrl; }
    public String getLocation(){ return location; }   public void setLocation(String location){ this.location = location; }
    public String getCategory(){ return category; }   public void setCategory(String category){ this.category = category; }
    public String getDetail(){ return detail; }       public void setDetail(String detail){ this.detail = detail; }
    public String getHours(){ return hours; }         public void setHours(String hours){ this.hours = hours; }
    public String getFee(){ return fee; }             public void setFee(String fee){ this.fee = fee; }

}
