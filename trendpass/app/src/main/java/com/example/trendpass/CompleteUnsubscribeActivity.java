package com.example.trendpass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.ui.login.LoginActivity;

public class CompleteUnsubscribeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_unsubscribe);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Button sendBtn = (Button) findViewById(R.id.relogin_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            //String ip = getString(R.string.ip);

            public void onClick(View v) {

                Intent intent = new Intent(CompleteUnsubscribeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

