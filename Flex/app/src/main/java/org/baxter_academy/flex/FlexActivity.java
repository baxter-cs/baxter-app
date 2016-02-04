package org.baxter_academy.flex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class FlexActivity extends AppCompatActivity {

    private boolean debugFirstRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flex_layout);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Checks if run is good after installation
        SharedPreferences prefs = this.getSharedPreferences("meta", Context.MODE_PRIVATE);
        int firstRun = prefs.getInt("firstRun", 0); //0 is the default value
        SharedPreferences.Editor editor = prefs.edit();
        if (firstRun == 0) {
            // Run the tutorial or something
        }

        refreshTaskList(getApplicationContext());

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

    public void refreshTaskList(Context context) {
        try {
            JsonObject jsonObject = GETHelper.getTasks(context);
            JsonObject meta = (JsonObject) jsonObject.get("meta");

            SharedPreferences prefs = this.getSharedPreferences("meta", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putBoolean("isInitTodo", meta.get("isInitTodo").getAsBoolean());
            editor.putBoolean("isInitDoing", meta.get("isInitDoing").getAsBoolean());
            editor.putBoolean("isInitDone", meta.get("isInitDone").getAsBoolean());
            editor.commit();

            Type listType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> tasks = new Gson().fromJson(jsonObject.get("tasks"), listType);
            TaskStorage task_storage = new TaskStorage();

            for (JsonObject task : tasks) {
                int mID = task.get("mID").getAsInt();
                String mTask = task.get("mTask").getAsString();
                String mDescription = task.get("mDescription").getAsString();
                String mAssignee = task.get("mAssignee").getAsString();
                String mDueDate = task.get("mDueDate").getAsString();
                String mTaskStatus = task.get("mTaskStatus").getAsString();
                Task newTask = new Task();
                newTask.addTask(mTask, mDescription, mAssignee, mDueDate, mID, mTaskStatus);
                task_storage.tasks.add(newTask);
            }

            // Creating a Gson object (Google's JSON Library)
            Gson gson = new Gson();
            // This saves our encoded json string into the shared pref. meta with the key "tasks"
            // This will be where we store our tasks
            editor.putString("tasks", gson.toJson(task_storage));
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
            refreshTaskList(context);
        }
    }

}
