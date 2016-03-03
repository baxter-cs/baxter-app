package org.baxter_academy.flex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Iterator;

public class FragmentDone extends Fragment {

    LinearLayout titleLayout;
    LinearLayout linearLayout;

    public FragmentDone() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done, container, false);

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
            TextView tv = (TextView) view.findViewById(R.id.title_done);
            if (prefs.getBoolean("isInitDone", false)) {
                tv.setVisibility(View.GONE);
            }
            // Here we iterate through all the Task objects in our list
            for(Iterator<Task> i = task_storage.tasks.iterator(); i.hasNext();) {
                final Task task = i.next();
                if (task.getTaskStatus().equals(Constants.title_done)) {
                    LinearLayout layout = (LinearLayout) view.findViewById(R.id.todo_layout);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ViewGroup.LayoutParams titleParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    titleLayout = new LinearLayout(getActivity());
                    titleLayout.setOrientation(LinearLayout.HORIZONTAL);
                    titleLayout.setLayoutParams(layoutParams);

                    linearLayout = new LinearLayout(getActivity());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setLayoutParams(layoutParams);

                    TextView textViewTitle = new TextView(getActivity());
                    textViewTitle.setLayoutParams(titleParams);
                    textViewTitle.setTextSize(20);
                    textViewTitle.setText(" " + task.getTaskTitle());
                    textViewTitle.setTextColor(Color.parseColor(Constants.task_textTitle));
                    textViewTitle.setBackgroundColor(Color.parseColor(Constants.task_bgTitle_done));
                    textViewTitle.setPadding(15, 15, 15, 10);
                    textViewTitle.setMovementMethod(new ScrollingMovementMethod());
                    titleLayout.addView(textViewTitle);

                    TextView textViewInfo = new TextView(getActivity());
                    textViewInfo.setLayoutParams(layoutParams);
                    textViewInfo.setTextSize(18);
                    textViewInfo.setText(task.getTaskInfo());
                    textViewInfo.setTextColor(Color.parseColor(Constants.task_textBody));
                    textViewInfo.setBackgroundColor(Color.parseColor(Constants.task_bgBody_done));
                    textViewInfo.setPadding(15, 5, 15, 15);
                    textViewInfo.setMovementMethod(new ScrollingMovementMethod());
                    linearLayout.addView(textViewInfo);

                    textViewInfo.setClickable(true);
                    textViewInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                            // Inflate the popup menu with xml file
                            popupMenu.getMenuInflater().inflate(R.menu.menu_done, popupMenu.getMenu());

                            // Add popupMenu with onMenuItemClickListener
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    SharedPreferences prefs = getActivity().getSharedPreferences("meta", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    Gson gson = new Gson();
                                    ClientHelper client = new ClientHelper();

                                    switch (item.getItemId()) {
                                        case R.id.delete:
                                            client.deleteTask(task.getTaskID().toString(), prefs.getString("uuid", "invalid"));
                                            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                            Intent refreshDelete = new Intent(getContext(), FlexActivity.class);
                                            startActivity(refreshDelete);
                                            break;
                                        default:
                                            Toast.makeText(getActivity(), item.getTitle() + " Clicked", Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent = new Intent(getContext(), FlexActivity.class);
                                    startActivity(intent);
                                    return true;
                                }
                            });

                            popupMenu.show();
                        }
                    });

                    TextView textViewBreak = new TextView(getActivity());
                    textViewBreak.setLayoutParams(layoutParams);
                    textViewBreak.setText(Constants.text_break);
                    textViewBreak.setPadding(15, 4, 15, 4);
                    textViewBreak.setMovementMethod(new ScrollingMovementMethod());
                    linearLayout.addView(textViewBreak);

                    layout.addView(titleLayout);
                    layout.addView(linearLayout);
                }
            }
        } else {
            TextView tv = (TextView) view.findViewById(R.id.title_done);
            tv.setText(json);
        }

        return view;
    }

}
