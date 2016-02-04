package org.baxter_academy.flex;

import android.app.DatePickerDialog;

import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.NavUtils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.text.InputType;

import android.view.MenuItem;
import android.view.View;

import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText toDate;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add a Task");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        findViewsById();
        setDateTimeField();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findViewsById(){
        toDate = (EditText) findViewById(R.id.bar_due);
        toDate.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField(){
        toDate.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view){
        if(view == toDate){
            toDatePickerDialog.show();
        }
    }

    public void createTask(View view){
        final EditText mTitle, mDescription, mAssignee, mDueDate;
        final String title, description, assignee, dueDate;

        Intent intent = new Intent(AddTaskActivity.this, FlexActivity.class);


        mTitle = (EditText) findViewById(R.id.bar_title);
        mDescription = (EditText) findViewById(R.id.bar_description);
        mAssignee = (EditText) findViewById(R.id.bar_assignee);
        mDueDate = (EditText) findViewById(R.id.bar_due);

        title = mTitle.getText().toString();
        description = mDescription.getText().toString();
        assignee = mAssignee.getText().toString();
        dueDate = mDueDate.getText().toString();


        POSTHelper.postNewTask(this, title, description, assignee, dueDate);

        startActivity(intent);
    }
}
