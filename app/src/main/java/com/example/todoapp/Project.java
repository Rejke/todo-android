package com.example.todoapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Project {
    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;
}
