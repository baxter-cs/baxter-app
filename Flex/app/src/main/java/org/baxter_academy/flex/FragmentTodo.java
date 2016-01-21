package org.baxter_academy.flex;

import android.graphics.Color;
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
            if(tv.getText().equals("To Do")) {
                tv.setTextSize(18);
                tv.setText(task.getTaskTitle());
                tv.append(task.getTaskInfo());
                tv.setBackgroundColor(Color.parseColor("#F8BBD0"));
                tv.setPadding(12, 12, 12, 12);
                tv.setTextColor(Color.parseColor("#212121"));
            }else {
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
                textView.setBackgroundColor(Color.parseColor("#F8BBD0"));
                textView.setPadding(12, 12, 12, 12);
                textView.setTextColor(Color.parseColor("#212121"));
                linearLayout.addView(textView);

                layout.addView(linearLayout);
            }
        }
        return view;
    }

}
