package org.baxter_academy.flex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FragmentTodo extends Fragment {

    LinearLayout linearLayout;

    public FragmentTodo() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        // Gets the json string - We're using getActivity instead of this because this doesn't work in this
        SharedPreferences prefs = getActivity().getSharedPreferences("meta", Context.MODE_PRIVATE);
        String json = prefs.getString("tasks", "error");

        // Error will only happen if the Preference does not exist
        if (!json.equals("error")) {
            // Here we create our Gson object
            Gson gson = new Gson();
            // Here we use our Gson object to decode our json string back into our TaskStorage class
            final TaskStorage task_storage = gson.fromJson(json, TaskStorage.class);
            // Here we set tv as our text view
            TextView tv = (TextView) view.findViewById(R.id.title_todo);
            if (tv.getText().equals(Constants.title_todo)) {
                tv.setVisibility(View.GONE);
            }
            // Here we iterate through all the Task objects in our list
            for(Iterator<Task> i = task_storage.tasks.iterator(); i.hasNext();) {
                final Task task = i.next();
                if (task.getTaskStatus().equals(Constants.title_todo)) {
                    LinearLayout layout = (LinearLayout) view.findViewById(R.id.todo_layout);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearLayout = new LinearLayout(getActivity());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setLayoutParams(layoutParams);

                    TextView textViewTitle = new TextView(getActivity());
                    textViewTitle.setLayoutParams(layoutParams);
                    textViewTitle.setTextSize(20);
                    textViewTitle.setText(" " + task.getTaskTitle());
                    textViewTitle.setTextColor(Color.parseColor(Constants.task_titleCol));
                    textViewTitle.setBackgroundColor(Color.parseColor(Constants.task_bg));
                    textViewTitle.setPadding(15, 15, 15, 0);
                    textViewTitle.setMovementMethod(new ScrollingMovementMethod());
                    linearLayout.addView(textViewTitle);

                    TextView textViewInfo = new TextView(getActivity());
                    textViewInfo.setLayoutParams(layoutParams);
                    textViewInfo.setTextSize(18);
                    textViewInfo.setText(task.getTaskInfo());
                    textViewInfo.setTextColor(Color.parseColor(Constants.task_textCol));
                    textViewInfo.setBackgroundColor(Color.parseColor(Constants.task_bg));
                    textViewInfo.setPadding(15, 0, 15, 15);
                    textViewInfo.setMovementMethod(new ScrollingMovementMethod());
                    linearLayout.addView(textViewInfo);

                    /*
                    Button optionButton = new Button(getActivity());
                    optionButton.setBackground(getResources().getDrawable(R.drawable.ic_more_vert_white_36dp));
                    optionButton.setGravity(Gravity.END);
                    linearLayout.addView(optionButton);
                    */

                    Button deleteButton = new Button(getActivity());
                    deleteButton.setTag(task.getTaskID());
                    deleteButton.setText("Delete " + deleteButton.getTag());
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        // This is run when the Button is pressed
                        @Override
                        public void onClick(View v) {
                            SharedPreferences prefs = getActivity().getSharedPreferences("meta", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            String json = prefs.getString("tasks", "error");
                            Gson gson = new Gson();
                            TaskStorage task_storage = gson.fromJson(json, TaskStorage.class);
                            List<Task> filteredTasks = new ArrayList<Task>();
                            for(Iterator<Task> i = task_storage.tasks.iterator(); i.hasNext();) {
                                Task filteredTask = i.next();
                                if (!filteredTask.getTaskID().equals(v.getTag())) {
                                    filteredTasks.add(filteredTask);
                                }
                            }
                            task_storage.tasks = filteredTasks;
                            // Saves the updated Task Storage
                            editor.putString("tasks", gson.toJson(task_storage));
                            editor.commit();
                            Intent intent = new Intent(getContext(), FlexActivity.class);
                            startActivity(intent);
                        }
                    });
                    linearLayout.addView(deleteButton);

                    Button upgradeStatusButton = new Button(getActivity());
                    upgradeStatusButton.setText("Upgrade Status");
                    upgradeStatusButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            task.upgradeStatus();
                            Gson gson = new Gson();
                            SharedPreferences prefs = getActivity().getSharedPreferences("meta", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("tasks", gson.toJson(task_storage));
                            editor.commit();
                            Intent intent = new Intent(getContext(), FlexActivity.class);
                            startActivity(intent);
                        }
                    });
                    linearLayout.addView(upgradeStatusButton);

                    layout.addView(linearLayout);
                }
            }

            // Make the list
            final List<String> list = new ArrayList<>();
            list.add("Default");
            list.add("Delete");
            list.add("To In Process");
            // Create the spinner
            Spinner spinner = (Spinner) view.findViewById(R.id.spinner_todo);
            //Spinner spinner = new Spinner(getActivity());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

        } else {
            TextView tv = (TextView) view.findViewById(R.id.title_todo);
            tv.setText(json);
        }

        return view;
    }

}
