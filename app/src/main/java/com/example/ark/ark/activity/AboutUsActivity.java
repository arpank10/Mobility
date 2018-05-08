package com.example.ark.ark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.ark.ark.R;

/**
 * Created by ark on 4/8/17.
 */

public class AboutUsActivity extends AppCompatActivity {
    TextView y;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        y=(TextView)findViewById(R.id.why);
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Information.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
