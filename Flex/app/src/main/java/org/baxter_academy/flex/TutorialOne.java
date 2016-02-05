package org.baxter_academy.flex;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorialOne extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_one_layout);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new MyClass());
    }

    public class MyClass implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), TutorialTwo.class);
            startActivity(intent);
        }

    }
}

