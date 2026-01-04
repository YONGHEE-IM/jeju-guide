package com.koreait.jejuguide.foods;

import java.util.List;

public class RestaurantDto {
    private String title;
    private String category;
    private String location;
    private String description;
    private String imageFile;        // 대표 이미지 파일명
    private java.util.List<String> imageFiles; // 갤러리 파일명들
    private String hours;
    private String price;

    public RestaurantDto(){}

    public static RestaurantDto of(String title, String category, String location,
                                   String description, String imageFile, java.util.List<String> imageFiles,
                                   String hours, String price){
        RestaurantDto d = new RestaurantDto();
        d.title = title; d.category = category; d.location = location;
        d.description = description; d.imageFile = imageFile; d.imageFiles = imageFiles;
        d.hours = hours; d.price = price;
        return d;
    }
    // getters & setters
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public String getLocation(){return location;} public void setLocation(String v){location=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getImageFile(){return imageFile;} public void setImageFile(String v){imageFile=v;}
    public java.util.List<String> getImageFiles(){return imageFiles;} public void setImageFiles(java.util.List<String> v){imageFiles=v;}
    public String getHours(){return hours;} public void setHours(String v){hours=v;}
    public String getPrice(){return price;} public void setPrice(String v){price=v;}
}
