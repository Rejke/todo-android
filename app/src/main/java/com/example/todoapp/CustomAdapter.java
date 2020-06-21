package com.example.todoapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.TreeSet;

public class CustomAdapter extends BaseAdapter {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private ArrayList<Todo> todos = new ArrayList<Todo>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;
    private Context mainActivityContext;

    public CustomAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainActivityContext = context;
    }

    public void addItem(final String item, Todo todo) {
        mData.add(item);
        todos.add(todo);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        todos.add(null);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType){
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.snippet_item1, null);

                    Todo currentTodo = todos.get(position);

                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                    holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                    holder.checkBox.setChecked(currentTodo.isCompleted);

                    if (currentTodo.isCompleted)
                        holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    TextView textView = holder.textView;

                    holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (currentTodo.isCompleted == isChecked)
                            return;

                        if (isChecked) {
                            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        } else {
                            textView.setPaintFlags(0);
                        }

                        JsonObject json = new JsonObject();
                        json.addProperty("is_completed", isChecked);

                        Ion.with(mainActivityContext)
                                .load("PATCH", mainActivityContext.getString(R.string.todoUpdateRequest) + currentTodo.getId())
                                .setJsonObjectBody(json)
                                .asJsonObject();

                        currentTodo.isCompleted = isChecked;
                    });
                    break;

                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.snippet_item2, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    break;
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(mData.get(position));

        return convertView;
    }



    public static class ViewHolder {
        public TextView textView;
        public CheckBox checkBox;
    }
}
