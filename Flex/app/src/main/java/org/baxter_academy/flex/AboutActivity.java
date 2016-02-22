package org.baxter_academy.flex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);

        TextView src = (TextView) findViewById(R.id.text_version);
        src.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
