package org.baxter_academy.flex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wil on 1/19/16.
 */
public class Task {

    String mTask, mDescription, mAssignee, mDueDate;

    Map<String, String> task = new HashMap<>();

    public void AddTask(String mTask, String mDescription, String mAssignee, String mDueDate){
        task.put("Title", mTask);
        task.put("Description", mDescription);
        task.put("Assignee", mAssignee);
        task.put("Due On", mDueDate);
    }

}
