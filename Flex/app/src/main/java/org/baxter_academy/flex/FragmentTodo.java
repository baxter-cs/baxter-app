package org.baxter_academy.flex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentTodo extends Fragment {

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        FlexActivity flexActivity = (FlexActivity) getActivity();
        String task_title = flexActivity.getTaskTitle();

        if(task_title != null) {
            TextView tv = (TextView) view.findViewById(R.id.testing);
            tv.setText(task_title);
        }

        return view;
    }

}
