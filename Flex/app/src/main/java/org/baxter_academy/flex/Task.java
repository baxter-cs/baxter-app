package org.baxter_academy.flex;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wil on 1/19/16.
 */
public class Task {

    Map<String, String> tasklist = new HashMap<String, String>();

    public void AddTask(String task, String description, String assignee, String dueDate){
        tasklist.put("Title", task);
        tasklist.put("Description", description);
        tasklist.put("Assignee", assignee);
        tasklist.put("Due On", dueDate);
    }

}
