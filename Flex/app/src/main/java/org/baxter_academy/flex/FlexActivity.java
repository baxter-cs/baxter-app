package org.baxter_academy.flex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.Iterator;

public class FlexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flex_layout);

        // Checking installation run
        SharedPreferences prefs = this.getSharedPreferences("meta", Context.MODE_PRIVATE);
        int firstRun = prefs.getInt("firstRun", 0);
        SharedPreferences.Editor editor = prefs.edit();
        if (firstRun == 0) {
            // This runs when Flex starts at the first time
            firstRun = 1;
            editor.putInt("firstRun", firstRun);
            editor.putBoolean("isInitTodo", false);
            editor.putBoolean("isInitDoing", false);
            editor.putBoolean("isInitDone", false);
            editor.commit();

            // Setting up default json files
            TaskStorage task_storage = new TaskStorage();
            // Creating initial Tasks
            Task starterTask = new Task();
            Task tutorialTask = new Task();
            starterTask.addTask("Welcome to Flex", "First Task!", "Flex Team", "02-05-2016", task_storage.getNewTaskID());
            tutorialTask.addTask("Learn to Use Flex", "Tap on this Task", "You", "02-05-2016", task_storage.getNewTaskID());

            // Adding initial Tasks to TaskStorage object
            task_storage.tasks.add(starterTask);
            task_storage.tasks.add(tutorialTask);

            // Creating a Gson object (Google's JSON Library)
            Gson gson = new Gson();

            // Saving encoded json string into shared preferences meta with key "tasks"
            // This will be where Tasks are stored
            editor.putString("tasks", gson.toJson(task_storage));
            editor.commit();
        } else if (false) {
            // This is for debugging purposes
            firstRun++;
            editor.putInt("firstRun", firstRun);
            editor.commit();
            String json = prefs.getString("tasks", "error");

            // Creating a Gson object
            Gson gson = new Gson();
            // Using Gson object to decode json string back into TaskStorage class
            TaskStorage task_storage = gson.fromJson(json, TaskStorage.class);
            // Creating a new task
            Task starterTask = new Task();
            starterTask.addTask("First Task", "This is the first task", "The Flex Team", "2-01-2016", task_storage.getNewTaskID());

            // Adding initial Tasks to TaskStorage object
            task_storage.tasks.add(starterTask);

            // Saving encoded json string into shared preferences meta with key "tasks"
            // This will be where tasks are stored
            editor.putString("tasks", gson.toJson(task_storage));
            editor.commit();
        }

        // Checking through all Tasks and updating initial states
        String json = prefs.getString("tasks", "error");
        Gson gson = new Gson();

        TaskStorage task_storage = gson.fromJson(json, TaskStorage.class);
        editor.putBoolean("isInitTodo", false);
        editor.putBoolean("isInitDoing", false);
        editor.putBoolean("isInitDone", false);
        for(Iterator<Task> i = task_storage.tasks.iterator(); i.hasNext();) {
            final Task task = i.next();
            if (task.getTaskStatus().equals(Constants.title_doing)) {
                editor.putBoolean("isInitDoing", true);
            } else if (task.getTaskStatus().equals(Constants.title_done)) {
                editor.putBoolean("isInitDone", true);
            } else if (task.getTaskStatus().equals(Constants.title_todo)) {
                editor.putBoolean("isInitTodo", true);
            }
        }
        editor.commit();

        // Creating the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Constants.app_name);

        // Expanding the tablayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(Constants.title_todo));
        tabLayout.addTab(tabLayout.newTab().setText(Constants.title_doing));
        tabLayout.addTab(tabLayout.newTab().setText(Constants.title_done));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Making the viewpager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final org.baxter_academy.flex.PagerAdapter adapter = new org.baxter_academy.flex.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.id_about:
                Intent aboutPage = new Intent(FlexActivity.this, AboutActivity.class);
                startActivity(aboutPage);
                return true;
            case R.id.id_add:
                Intent addTask = new Intent(FlexActivity.this, AddTaskActivity.class);
                startActivity(addTask);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getTaskJSON() {
        SharedPreferences prefs = this.getSharedPreferences("meta", Context.MODE_PRIVATE);
        return prefs.getString("tasks", "error");
    }

}
