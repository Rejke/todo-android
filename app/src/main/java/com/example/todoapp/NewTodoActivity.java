package com.example.todoapp;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class NewTodoActivity extends AppCompatActivity {
    private ArrayList<Project> projects = new ArrayList<Project>();
    private Project selectedProject;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

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

        setContentView(R.layout.activity_new_todo);

        ((TextView) findViewById(R.id.textSeparator)).setText("Категория");

        Bundle bundle = getIntent().getExtras();
        projects = bundle.getParcelableArrayList("projects");

        ListView listView = (ListView) this.findViewById(R.id.projects_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.projectlist_cell, R.id.text);

        for (Project project : projects) {
            if (project.getTitle().equals("Прочее"))
                selectedProject = project;

            adapter.add(project.getTitle());
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, pos, l) -> {
            for (int i = 0; i < adapterView.getCount(); i++) {
                LinearLayout linearLayout = adapterView.getChildAt(i).findViewById(R.id.linearlayout);
                TextView textView = adapterView.getChildAt(i).findViewById(R.id.text);
                if (pos == i) {
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.selectionColor));
                    textView.setBackgroundColor(getResources().getColor(R.color.selectionColor));

                    selectedProject = projects.get(i);
                } else {
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.systemWhiteColor));
                    textView.setBackgroundColor(getResources().getColor(R.color.systemWhiteColor));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.createBtn:
                String text = ((EditText) this.findViewById(R.id.todo_text)).getText().toString();

                if (text.length() == 0)
                    return false;

                JsonObject json = new JsonObject();
                json.addProperty("text", text);
                json.addProperty("is_completed", false);
                json.addProperty("project_id", selectedProject.getId());

                Ion.with(this)
                        .load("POST", getString(R.string.todoCreateRequest))
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback((e, result) -> {
                            this.finish();
                        });
                return true;
        }
        return false;
    }
}