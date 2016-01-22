package org.baxter_academy.flex;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Iterator;

public class FragmentTodo extends Fragment {

    LinearLayout linearLayout;
    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    public FragmentTodo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        // Gets the json string
        SharedPreferences prefs = getActivity().getSharedPreferences("meta", Context.MODE_PRIVATE);
        String json = prefs.getString("tasks", "error");
        // Error will only happen if the Preference does not exist
        if (json != "error") {
            // Here we create our Gson object
            Gson gson = new Gson();
            // Here we use our Gson object to decode our json string back into our TaskStorage class
            TaskStorage task_storage = gson.fromJson(json, TaskStorage.class);
            // Here we set tv as our text view
            TextView tv = (TextView) view.findViewById(R.id.title_todo);
            tv.setTextSize(18);
            String foobar = "";
            // Here we iterate through all the Task objects in our list
            for(Iterator<Task> i = task_storage.tasks.iterator(); i.hasNext();) {
                foobar += i.next().getTaskTitle();
            }
            tv.setText(foobar);
        } else {
            TextView tv = (TextView) view.findViewById(R.id.title_todo);
            tv.setTextSize(18);
            tv.setText(json);
        }

        // Gets the task
        /* Serializable mTask = flexActivity.getTask();
        Task task = (Task) mTask;
        if(task != null){
            // This is the thing that says Todo
            TextView tv = (TextView) view.findViewById(R.id.title_todo);
            if(tv.getText().equals("To Do")) {
                // Here it's text size is being changed
                tv.setTextSize(18);
                // here the title is being added on to it
                tv.setText(task.getTaskTitle());
                // Here is the task info being added on to it
                tv.append(task.getTaskInfo());
                // Here is it's background color being changed, rememebr this is just 1 textview lol
                tv.setBackgroundColor(Color.parseColor("#F8BBD0"));
                // Here it's being made to look fancy
                tv.setPadding(12, 12, 12, 12);
                tv.setTextColor(Color.parseColor("#212121"));
            }
        }*/
        return view;
    }

}
