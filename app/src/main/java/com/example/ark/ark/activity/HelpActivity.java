package com.example.ark.ark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.ark.ark.help.help_bugs;
import com.example.ark.ark.help.help_recording;
import com.example.ark.ark.help.help_sensors;
import com.example.ark.ark.R;

/**
 * Created by ark on 4/8/17.
 */

public class HelpActivity extends AppCompatActivity {
    TextView recording,sensors,bugs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        recording= (TextView)findViewById(R.id.recording);
        sensors=(TextView) findViewById(R.id.sensors);
        bugs=(TextView) findViewById(R.id.bugs);
        recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),help_recording.class);
                startActivity(intent);
            }
        });
        sensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),help_sensors.class);
                startActivity(intent);
            }
        });
        bugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),help_bugs.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
