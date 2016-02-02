package org.baxter_academy.flex;

import java.io.Serializable;

/**
 * Created by wil on 1/19/16.
 */
public class Task implements Serializable{

    private String mTask, mDescription, mAssignee, mDueDate, mTaskStatus;

    public void addTask(String mTask, String mDescription, String mAssignee, String mDueDate){
        this.mTask = mTask;
        this.mDescription = mDescription;
        this.mAssignee = mAssignee;
        this.mDueDate = mDueDate;
        this.mTaskStatus = "To Do";
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

    @Override
    public String toString() {
        return " " + mTask + "\n Description: " + mDescription + "\n Assignee: " + mAssignee + "\n Due On: " + mDueDate;
    }
}
