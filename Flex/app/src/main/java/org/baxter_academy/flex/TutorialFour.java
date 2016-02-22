package org.baxter_academy.flex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TutorialFour extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_four_layout);
        TextView textViewInfo = (TextView) findViewById(R.id.doneInfoTaskExample);
        textViewInfo.setClickable(true);
        textViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                // Inflate the popup menu with xml file
                popupMenu.getMenuInflater().inflate(R.menu.menu_done, popupMenu.getMenu());

                // Add popupMenu with onMenuItemClickListener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.delete:
                                Toast.makeText(v.getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(v.getContext(), item.getTitle() + " Clicked", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(v.getContext(), FlexActivity.class);
                        startActivity(intent);
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }
}
