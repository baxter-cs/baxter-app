package org.baxter_academy.flex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

public class FragmentTodo extends Fragment {

    LinearLayout linearLayout;

    public FragmentTodo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        FlexActivity flexActivity = (FlexActivity) getActivity();
        Serializable mTask = flexActivity.getTask();
        Task task = (Task) mTask;
        if(task != null){

            TextView tv = (TextView) view.findViewById(R.id.title_todo);
            tv.setText(task.toString());

            //TODO: Begins
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.todo_layout);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout = new LinearLayout(getActivity());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(layoutParams);

            TextView taskTitle = new TextView(getActivity());
            taskTitle.setLayoutParams(layoutParams);
            taskTitle.setText(task.getTaskTitle());
            linearLayout.addView(taskTitle);
            layout.addView(linearLayout);

            //TODO: Ends

        }
        return view;
    }

    public void createTask(){

    }

}
