package org.baxter_academy.flex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FlexActivity extends AppCompatActivity {
    TextView textView;

    public void helloWorld(View view){
        String button_text;
        button_text = ((Button) view).getText().toString();
        if(button_text.equals("Open Minor Activity")){
            Intent intent = new Intent(this, MinorActivity.class);
            startActivity(intent);
        }
    }

    public void sendMessage(){
        //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flex_layout);
    }
}
