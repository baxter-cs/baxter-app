package org.baxter_academy.flex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.EditText;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signIn (View view) {
        final String username, password, response;

        EditText editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        username = editTextUsername.getText().toString();
        password = editTextPassword.getText().toString();

        response = new ClientHelper().login(username, password);

        SharedPreferences prefs = this.getSharedPreferences("meta", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("uuid", response);
        editor.commit();

        Intent flexActivity = new Intent(LoginActivity.this, FlexActivity.class);
        startActivity(flexActivity);
    }
}
