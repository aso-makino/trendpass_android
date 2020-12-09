package com.example.trendpass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.Button;

import java.net.MalformedURLException;
import java.net.URL;

public class CompleteUnsubscribeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_unsubscribe);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Button sendBtn = (Button) findViewById(R.id.button3);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            //String ip = getString(R.string.ip);

            public void onClick(View v) {

                Intent intent = new Intent(CompleteUnsubscribeActivity.this, Activity.class);
                startActivity(intent);
            }
        });
    }
}