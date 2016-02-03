package org.baxter_academy.flex;

import java.io.Serializable;

public class Task implements Serializable{

    private String mTask, mDescription, mAssignee, mDueDate, mTaskStatus;
    private int mID;
    private static boolean isInitDoing, isInitDone;

    public void addTask(String mTask, String mDescription, String mAssignee, String mDueDate, Integer mID){
        this.mTask = mTask;
        this.mDescription = mDescription;
        this.mAssignee = mAssignee;
        this.mDueDate = mDueDate;
        this.mTaskStatus = Constants.title_todo;
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
        if (mTaskStatus.equals(Constants.title_todo)) {
            mTaskStatus = Constants.title_doing;
            isInitDoing = true;
        } else if (mTaskStatus.equals(Constants.title_doing)) {
            mTaskStatus = Constants.title_done;
            isInitDone = true;
        } else if (mTaskStatus.equals(Constants.title_done)) {
            // Delete the Task or something
        }
    }

    public static boolean isInitDoing() {
        return isInitDoing;
    }

    public static boolean isInitDone() {
        return isInitDone;
    }

    public Integer getTaskID() {
        return mID;
    }

    @Override
    public String toString() {
        return " " + mTask + "\n Description: " + mDescription + "\n Assignee: " + mAssignee + "\n Due On: " + mDueDate;
    }
}
