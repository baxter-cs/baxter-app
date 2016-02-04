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
import java.util.List;

public class FlexActivity extends AppCompatActivity {

    private boolean debugFirstRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flex_layout);

        // Checks if run is good after installation
        SharedPreferences prefs = this.getSharedPreferences("meta", Context.MODE_PRIVATE);
        int firstRun = prefs.getInt("firstRun", 0); //0 is the default value
        SharedPreferences.Editor editor = prefs.edit();
        if (firstRun == 0) {
            // This runs when the app starts the first time
            firstRun = 1;
            editor.putInt("firstRun", firstRun);
            editor.putBoolean("isInitTodo", false);
            editor.putBoolean("isInitDoing", false);
            editor.putBoolean("isInitDone", false);
            editor.commit();

            // Setting up default json files
            TaskStorage task_storage = new TaskStorage();
            // Creating Starter Task
            Task starterTask = new Task();
            starterTask.addTask("Welcome to Flex", "First Task", "The Flex Team", "2-01-2016", task_storage.getNewTaskID());
            // Adding Starter Task to TaskStorage class
            task_storage.tasks.add(starterTask);

            // Creating a Gson object (Google's JSON Library)
            Gson gson = new Gson();
            // This saves our encoded json string into the shared pref. meta with the key "tasks"
            // This will be where we store our tasks
            editor.putString("tasks", gson.toJson(task_storage));
            editor.commit();
        } else if (debugFirstRun) {
            // This is for debugging purposes
            firstRun++;
            editor.putInt("firstRun", firstRun);
            editor.commit();
            String json = prefs.getString("tasks", "error");

            // Create a Gson object
            Gson gson = new Gson();
            // Use our Gson object to decode our json string back into our TaskStorage class
            TaskStorage task_storage = gson.fromJson(json, TaskStorage.class);
            // Creating a new task
            Task starter_task = new Task();
            starter_task.addTask("First Task", "This is the first task", "The Flex Team", "2-01-2016", task_storage.getNewTaskID());
            // Init. our TaskStorage class
            task_storage.tasks.add(starter_task);

            // This saves our encoded json string into the shared pref. meta with the key "tasks"
            // This will be where we store our tasks
            editor.putString("tasks", gson.toJson(task_storage));
            editor.commit();
        }

        // Looks through all the tasks and updates the init booleans
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

        // Create the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Constants.app_name);

        // Expand the tablayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(Constants.title_todo));
        tabLayout.addTab(tabLayout.newTab().setText(Constants.title_doing));
        tabLayout.addTab(tabLayout.newTab().setText(Constants.title_done));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Make the viewpager
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
            case R.id.action_settings:
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
