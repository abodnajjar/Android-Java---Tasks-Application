package com.example.assigmentone;

import java.io.Serializable;
import java.util.Date;

public class Task  implements Serializable {
    private String title;
    private String description;
    private String date;
    private boolean state;
    private String priority;
    //I use static image
    private String imageurl;

    public Task(String priority, String date, String description, boolean state, String title) {
        this.priority = priority;
        this.date = date;
        this.description = description;
        this.state = state;
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageID() {
        return imageurl;
    }

    public void setImageID(String imageID) {
        this.imageurl = imageID;
    }
    public int getPriorityValue() {
        switch(priority) {
            case "High": return 3;
            case "Medium": return 2;
            default: return 1; // Low
        }
    }
}

