package org.baxter_academy.flex;

import java.util.ArrayList;
import java.util.List;

public class TaskStorage {
    List<Task> tasks = new ArrayList<>();
    private int newTaskID = 0;

    public int getNewTaskID() {
        newTaskID++;
        return newTaskID;
    }
}
