package org.baxter_academy.flex;

import java.io.Serializable;

/**
 * Created by wil on 1/19/16.
 */
public class Task implements Serializable{

    private String mTask, mDescription, mAssignee, mDueDate, mTaskStatus;
    private int mID;

    public void addTask(String mTask, String mDescription, String mAssignee, String mDueDate, Integer mID){
        this.mTask = mTask;
        this.mDescription = mDescription;
        this.mAssignee = mAssignee;
        this.mDueDate = mDueDate;
        this.mTaskStatus = "To Do";
        this.mID = mID;
    }

    public String getTaskTitle(){
        return " " + mTask;
    }

    public String getTaskInfo() {
        return "\n Description: " + mDescription + "\n Assignee: " + mAssignee + "\n Due On: " + mDueDate;
    }

    public String getTaskDescription(){
        return " " + mDescription;
    }

    public String getTaskAssignee(){
        return " " + mAssignee;
    }

    public String getTaskDueDate(){
        return " " + mDueDate;
    }

    public String getTaskStatus() {
        return mTaskStatus;
    }

    public void upgradeStatus() {
        if (mTaskStatus.equals("To Do")) {
            mTaskStatus = "In Progress";
        } else if (mTaskStatus.equals("In Progress")) {
            mTaskStatus = "Done";
        } else if (mTaskStatus.equals("Done")) {
            // Delete the Task or something
        }
    }

    public Integer getTaskID() {
        return mID;
    }

    @Override
    public String toString() {
        return " " + mTask + "\n Description: " + mDescription + "\n Assignee: " + mAssignee + "\n Due On: " + mDueDate;
    }
}
