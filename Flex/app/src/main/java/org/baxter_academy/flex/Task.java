package org.baxter_academy.flex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String getTitle(){
        return mTask;
    }

    public String getDescription(){
        return mDescription;
    }

    public String getAssignee(){
        return mAssignee;
    }

    public String getDueDate(){
        return mDueDate;
    }

    @Override
    public String toString() {
        return "Title: " + mTask + " [Description: " + mDescription + ". Assignee: " + mAssignee + ". Due On: " + mDueDate + "]";
    }
}
