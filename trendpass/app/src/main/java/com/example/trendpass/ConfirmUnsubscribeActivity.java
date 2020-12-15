package com.example.trendpass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.async.AsyncDelete;

import java.net.MalformedURLException;
import java.net.URL;

public class ConfirmUnsubscribeActivity extends AppCompatActivity {

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_unsubscribe);
        //　ユーザーIDを取得
        SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
        userId = loginData.getString("userId", "");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Button sendBtn = (Button) findViewById(R.id.unsubscribe_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            String ip = getString(R.string.ip);

            public void onClick(View v) {
                try {
                    new AsyncDelete(ConfirmUnsubscribeActivity.this)
                            .execute(new URL("http://" + ip + ":8080/trendpass/Unsubscribe?userId=" + userId ));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}