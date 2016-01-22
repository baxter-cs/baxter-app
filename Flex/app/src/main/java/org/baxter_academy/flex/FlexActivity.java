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

import java.io.Serializable;

public class FlexActivity extends AppCompatActivity {

    private Serializable task;
    public boolean debugFirstRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Usual Setup stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flex_layout);
        // Checks if being run right after installation
        //getting preferences
        SharedPreferences prefs = this.getSharedPreferences("meta", Context.MODE_PRIVATE);
        int firstRun = prefs.getInt("firstRun", 0); //0 is the default value
        SharedPreferences.Editor editor = prefs.edit();
        if (firstRun == 0) {
            // This means this is the first time the app is run or somehow the shared pre. were wiped
            firstRun = 1;
            editor.putInt("firstRun", firstRun);
            editor.commit();
            // Setting up default json files
            TaskStorage task_storage = new TaskStorage();
            // Creating our Starter Task
            Task starter_task = new Task();
            starter_task.AddTask("Welcome to Flex", "We don't have a tutorial yet, but when we do make sure to check it out", "John", "1-21-2016");
            // Init. our TaskStorage class
            task_storage.tasks.add(starter_task);
            // Creating our Gson (Google's JSON Library)
            Gson gson = new Gson();
            // This saves our encoded json string into the shared pref. meta with the key "tasks"
            // This will be where we store our tasks
            editor.putString("tasks", gson.toJson(task_storage));
            editor.commit();
        } else if (debugFirstRun) {
            firstRun += 1;
            editor.putInt("firstRun", firstRun);
            editor.commit();
            String json = prefs.getString("tasks", "error");
            // Here we create our Gson object
            Gson gson = new Gson();
            // Here we use our Gson object to decode our json string back into our TaskStorage class
            TaskStorage task_storage = gson.fromJson(json, TaskStorage.class);
            // Creating our New Task
            Task starter_task = new Task();
            starter_task.AddTask("This is your " + Integer.toString(firstRun) + " Time Running This App", "OMG IT WORKED", "John", "1-21-2016");
            // Init. our TaskStorage class
            task_storage.tasks.add(starter_task);
            // This saves our encoded json string into the shared pref. meta with the key "tasks"
            // This will be where we store our tasks
            editor.putString("tasks", gson.toJson(task_storage));
            editor.commit();
        }
        // Names the bar thing that says Flex, yeah idk...
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Flex");
        // Creates the toolbar that Wil made
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("To Do"));
        tabLayout.addTab(tabLayout.newTab().setText("In Process"));
        tabLayout.addTab(tabLayout.newTab().setText("Done"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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
        /* No Longer Used
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            task = bundle.getSerializable("Task");
        }
        */
    }

    public Serializable getTask(){
        return task;
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

        switch(id){
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
