package org.baxter_academy.flex;

import java.io.Serializable;

/**
 * Created by wil on 1/19/16.
 */
public class Task implements Serializable{

    private String mTask, mDescription, mAssignee, mDueDate;

    public void AddTask(String mTask, String mDescription, String mAssignee, String mDueDate){
        this.mTask = mTask;
        this.mDescription = mDescription;
        this.mAssignee = mAssignee;
        this.mDueDate = mDueDate;
    }

    public String getTaskTitle(){
        return mTask;
    }

    public String getTaskDescription(){
        return mDescription;
    }

    public String getTaskAssignee(){
        return mAssignee;
    }

    public String getTaskDueDate(){
        return mDueDate;
    }

    @Override
    public String toString() {
        return "Title: " + mTask + "\n Description: " + mDescription + "\n Assignee: " + mAssignee + "\n Due On: " + mDueDate;
    }
}
