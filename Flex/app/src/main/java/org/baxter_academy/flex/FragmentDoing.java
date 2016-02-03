package org.baxter_academy.flex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FragmentDoing extends Fragment {

    LinearLayout linearLayout;

    public FragmentDoing() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doing, container, false);

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
            TextView tv = (TextView) view.findViewById(R.id.title_doing);
            if (tv.getText().equals(Constants.title_doing)) {
                tv.setVisibility(View.GONE);
            }
            // Here we iterate through all the Task objects in our list
            for(Iterator<Task> i = task_storage.tasks.iterator(); i.hasNext();) {
                final Task task = i.next();
                if (task.getTaskStatus().equals(Constants.title_doing)) {
                    // Wil's Code
                    LinearLayout layout = (LinearLayout) view.findViewById(R.id.todo_layout);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearLayout = new LinearLayout(getActivity());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setLayoutParams(layoutParams);

                    TextView textView = new TextView(getActivity());
                    textView.setLayoutParams(layoutParams);
                    textView.setTextSize(20);
                    textView.setText(task.getTaskTitle());
                    textView.append(task.getTaskInfo());
                    textView.setBackgroundColor(Color.parseColor(Constants.task_bg));
                    textView.setPadding(15, 15, 20, 20);
                    textView.setTextColor(Color.parseColor(Constants.task_textCol));
                    textView.setMovementMethod(new ScrollingMovementMethod());
                    linearLayout.addView(textView);

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
        } else {
            TextView tv = (TextView) view.findViewById(R.id.title_doing);
            tv.setText(json);
        }

        return view;
    }

}
