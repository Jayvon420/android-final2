package com.example.projectcode;

public class Upload {
    private String description;
    private String imageUrl;

    public Upload() {
        // Empty constructor needed for Firebase
    }

    public Upload(String description, String imageUrl) {
        if (description.trim().equals("")) {
            description = "No description";
        }
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}