package com.example.todoapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.koushikdutta.ion.Ion;
import com.scalified.fab.ActionButton;

import java.util.ArrayList;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter mAdapter;
    private ArrayList<Project> projects = new ArrayList<>();

    private void initFab() {
        ActionButton fab = findViewById(R.id.action_button);
        fab.setImageResource(R.drawable.fab_plus_icon);
        fab.setButtonColor(getResources().getColor(R.color.pinkSearch));
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewTodoActivity.class);
            intent.putParcelableArrayListExtra("projects", MainActivity.this.projects);
            startActivity(intent);
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/OpenSans-Light.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);

        setContentView(R.layout.activity_main);

        initFab();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAdapter = new CustomAdapter(this);

        Ion.with(this)
                .load(getString(R.string.projectsRequest))
                .asJsonArray()
                .setCallback((e, result) -> {
                    if (result == null) return;

                    projects = new ArrayList<Project>();

                    for (final JsonElement projectElement : result) {
                        projects.add(new Gson().fromJson(projectElement, Project.class));
                    }

                    Ion.with(MainActivity.this)
                            .load(getString(R.string.todoRequest))
                            .asJsonArray()
                            .setCallback((e1, result1) -> {
                                if (result1 == null) return;

                                ArrayList<Todo> todos = new ArrayList<Todo>();
                                for (final JsonElement todoElement : result1) {
                                    todos.add(new Gson().fromJson(todoElement, Todo.class));
                                }

                                for (int i = 0; i < projects.size(); i++) {
                                    mAdapter.addSectionHeaderItem(projects.get(i).getTitle());

                                    for (int j = 0; j < todos.size(); j++) {
                                        if (todos.get(j).getProjectId() == projects.get(i).getId()) {
                                            mAdapter.addItem(todos.get(j).getText(), todos.get(j).isCompleted);
                                        }
                                    }
                                }
                            });
                });

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(mAdapter);
    }
}