package com.example.todoapp;

import com.google.gson.annotations.SerializedName;

public class Todo {
    @SerializedName("id")
    public int id;

    @SerializedName("text")
    public String text;

    @SerializedName("is_completed")
    public boolean isCompleted;

    @SerializedName("project_id")
    public int projectId;

    public Todo() {
        isCompleted = false;
    }
}
