package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.scalified.fab.ActionButton;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter mAdapter;

    private void initFab() {
        ActionButton fab = findViewById(R.id.action_button);
        fab.setImageResource(R.drawable.fab_plus_icon);
        fab.setButtonColor(getResources().getColor(R.color.pinkSearch));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewTodoActivity.class));
            }
        });
    }

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

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);

        setContentView(R.layout.activity_main);

        initFab();

        mAdapter = new CustomAdapter(this);
        for (int i = 1; i < 10; i++) {
            mAdapter.addItem("Row Item #" + 1);
            if (i % 4 == 0) {
                mAdapter.addSectionHeaderItem("Section #" + 1);
            }
        }

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(mAdapter);
    }
}