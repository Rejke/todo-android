package com.example.todoapp;

import com.google.gson.annotations.SerializedName;

public class Todo {
    @SerializedName("id")
    private int id;
    @SerializedName("text")
    private String text;
    @SerializedName("is_completed")
    public boolean isCompleted;
    @SerializedName("project_id")
    private int projectId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Todo() {
        isCompleted = false;
    }
}
